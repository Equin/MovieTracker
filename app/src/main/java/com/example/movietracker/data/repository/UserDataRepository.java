package com.example.movietracker.data.repository;

import com.example.movietracker.data.database.MoviesDatabase;
import com.example.movietracker.data.database.dao.MovieDao;
import com.example.movietracker.data.database.dao.UserDao;
import com.example.movietracker.data.entity.movie.MarkMovieAsFavoriteResultEntity;
import com.example.movietracker.data.entity.movie.MarkMovieAsFavoriteRequestBodyEntity;
import com.example.movietracker.data.entity.movie.MovieResultEntity;
import com.example.movietracker.data.entity.movie.MoviesEntity;
import com.example.movietracker.data.entity.user.UserDetailsEntity;
import com.example.movietracker.data.entity.user.UserEntity;
import com.example.movietracker.data.entity.user.UserWithFavoriteMovies;
import com.example.movietracker.data.net.RestClient;
import com.example.movietracker.data.net.api.UserApi;
import com.example.movietracker.view.model.MarkAsFavoriteResultVariants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class UserDataRepository implements UserRepository {

    private static final int DB_REQUEST_TIMEOUT = 2;

    private UserDao userDao;
    private MovieDao movieDao;
    private UserApi userApi;

    private UserDataRepository() {}

    private static class SingletonHelper {
        private static final UserDataRepository INSTANCE = new UserDataRepository();
    }

    public static UserDataRepository getInstance(){
        return SingletonHelper.INSTANCE;
    }

    @Override
    public void init(RestClient restClient, MoviesDatabase moviesDatabase) {
        this.userDao = moviesDatabase.getUserDao();
        this.movieDao = moviesDatabase.getMovieDao();
        this.userApi = restClient.getUserApi();
    }

    /**
     * getting user from UserEntity table, with 2 seconds timeout, if no emit by that time it
     * throws TimeoutException, and in onErrorReturn returns default User
     * @return UserEntity
     */
    @Override
    public Observable<UserEntity> getUser() {
        return this.userDao.getUser().timeout(DB_REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .onErrorReturn(throwable -> {
                    UserEntity userEntity =  UserEntity.initialUser();
                    userEntity.setParentalControlEnabled(true);
                    this.userDao.addUser(userEntity);
                    return userEntity;
                });
    }

    /**
     * getting user with favorites from DB, if there is no favorites in UserWithFavoritesEntity table
     * -> gets user and checking his movieId field by getting movie from MovieResultEntity table
     * @return userEntity with favorite movies
     */
    @Override
    public Observable<UserEntity> getUserWithFavorites() {
        return this.userDao.getUserWithFavorites().map(userEntities -> {
            if (userEntities == null || userEntities.isEmpty()) {
                userEntities = new ArrayList<>();
                userEntities.add(this.userDao.getUserNotObservable());
            }

            UserEntity userEntity =  userEntities.get(0);

            for(int i = 0; i < userEntities.size(); i++) {
                MovieResultEntity movieResultEntity
                        = this.movieDao.getMovieById(
                        userEntities.get(i).getMovieId(), !userEntities.get(i).isParentalControlEnabled());

                if(movieResultEntity != null) {
                    movieResultEntity.setFavorite(true);
                    userEntity.addToFavorites(movieResultEntity);
                }
            }

            return userEntity;
        });
    }

    @Override
    public void addUser(UserEntity userEntity) {
        this.userDao.addUser(userEntity);
    }

    @Override
    public Completable updateUser(UserEntity userEntity) {
        if(userEntity.getFavoriteMovies() != null && !userEntity.getFavoriteMovies().isEmpty()) {
            this.userDao.addUserFavoriteMoviesRelation(userEntity);
        }

        return this.userDao.updateUser(userEntity);
    }

    @Override
    public Completable deleteMovieFromFavorites(UserWithFavoriteMovies userWithFavoriteMovies) {
        return this.userDao.deleteMovieFromFavorites(userWithFavoriteMovies);
    }

    @Override
    public Observable<MarkMovieAsFavoriteResultEntity> syncFavoritesWithServer(UserEntity userEntity) {
        return this.getUserWithFavorites().flatMap(userWithFavoritesFromDB ->
                this.getFavoritesFromServer(userEntity.getUserId(), userEntity.getSessionId(), 1)
                        .flatMap(moviesEntity -> syncAllMovieFavorites(moviesEntity, userWithFavoritesFromDB)));
    }

    private Observable<MarkMovieAsFavoriteResultEntity> syncAllMovieFavorites(
            MoviesEntity moviesEntityFromServer,
            UserEntity localUserWithFavorites) {
        List<Observable<MoviesEntity>> requests = new ArrayList<>();

        if (moviesEntityFromServer.getMovies().isEmpty()) {
            requests.add(addRequest(localUserWithFavorites, 1));
        } else {
            for (int i = 1; i <= moviesEntityFromServer.getTotalPages(); i++) {
                requests.add(addRequest(localUserWithFavorites, i));
            }
        }

        return Observable.zip(requests, moviesFromServer -> {
            MoviesEntity moviesEntity;
            List<MovieResultEntity> localFavoriteMovies = localUserWithFavorites.getFavoriteMovies();
            List<MarkMovieAsFavoriteRequestBodyEntity> markFavoriteRequestBodyEntities = new ArrayList<>();

            for (Object movie : moviesFromServer) {
                moviesEntity = ((MoviesEntity) movie);

                for(MovieResultEntity serverMovie : moviesEntity.getMovies() ) {
                    MarkMovieAsFavoriteRequestBodyEntity tempFavRequestBody
                            = new MarkMovieAsFavoriteRequestBodyEntity("movie", serverMovie.getMovieId(), false );
                    if (localFavoriteMovies != null) {
                        if (!localFavoriteMovies.contains(serverMovie)) {
                            markFavoriteRequestBodyEntities.add(tempFavRequestBody);
                        }
                    } else {
                        markFavoriteRequestBodyEntities.add(tempFavRequestBody);
                    }
                }

                if (localFavoriteMovies != null) {
                    for(MovieResultEntity localMovie : localFavoriteMovies ) {
                        if (!moviesEntity.getMovies().contains(localMovie)) {
                            markFavoriteRequestBodyEntities.add(
                                    new MarkMovieAsFavoriteRequestBodyEntity(
                                            "movie",
                                            localMovie.getMovieId(),
                                            true ));
                        }
                    }
                }
            }

            if (markFavoriteRequestBodyEntities.isEmpty()) {
                return new MarkMovieAsFavoriteResultEntity(
                        MarkAsFavoriteResultVariants.UP_TO_DATE.getResultMessage(),
                        MarkAsFavoriteResultVariants.UP_TO_DATE.getResultCode());
            }

            return markAsFavoriteMoviesOnTmdbServer(markFavoriteRequestBodyEntities, localUserWithFavorites);
        });
    }

    private MarkMovieAsFavoriteResultEntity markAsFavoriteMoviesOnTmdbServer(
            List<MarkMovieAsFavoriteRequestBodyEntity> markFavoriteRequestBodyEntities,
            UserEntity localUserWithFavorites) {
        List<Observable<MarkMovieAsFavoriteResultEntity>> favoriteRequest = new ArrayList<>();

        for (MarkMovieAsFavoriteRequestBodyEntity markMovieAsFavoriteRequestBodyEntity : markFavoriteRequestBodyEntities) {
            favoriteRequest.add(
                    addMarkAsFavoriteRequest(markMovieAsFavoriteRequestBodyEntity,
                            localUserWithFavorites));
        }

        return Observable.zip(favoriteRequest, requestResult -> {
            MarkMovieAsFavoriteResultEntity markMovieAsFavoriteResultEntity = new MarkMovieAsFavoriteResultEntity();
            for (Object favResult : requestResult) {
                markMovieAsFavoriteResultEntity = new MarkMovieAsFavoriteResultEntity(
                        MarkAsFavoriteResultVariants.UPDATE_SUCCESSFUL.getResultMessage(),
                        MarkAsFavoriteResultVariants.UPDATE_SUCCESSFUL.getResultCode());

                if (((MarkMovieAsFavoriteResultEntity) favResult).getStatusCode()
                        == MarkAsFavoriteResultVariants.AUTHORIZATION_FAILED.getResultCode()
                        || ((MarkMovieAsFavoriteResultEntity) favResult).getStatusCode()
                        == MarkAsFavoriteResultVariants.UPDATE_FAILED.getResultCode()) {
                    markMovieAsFavoriteResultEntity =(MarkMovieAsFavoriteResultEntity) favResult;
                }
            }
            return markMovieAsFavoriteResultEntity;
        }).blockingSingle();
    }

    private Observable<MarkMovieAsFavoriteResultEntity> addMarkAsFavoriteRequest(
            MarkMovieAsFavoriteRequestBodyEntity markMovieAsFavoriteRequestBodyEntity,
            UserEntity userEntity) {
        return this.markAsFavorite(
                userEntity.getUserId(),
                markMovieAsFavoriteRequestBodyEntity,
                userEntity.getSessionId());
    }

    private Observable<MoviesEntity> addRequest(UserEntity localUserWithFavorites, int page) {
        return this.getFavoritesFromServer(
                localUserWithFavorites.getUserId(),
                localUserWithFavorites.getSessionId(),
                page);
    }

    @Override
    public Single<UserDetailsEntity> getUserDetailsFromServer(UserEntity userEntity) {
        return this.userApi.getUserDetails(userEntity.getSessionId());
    }

    @Override
    public Observable<MarkMovieAsFavoriteResultEntity> markAsFavorite(
            int accountId,
            MarkMovieAsFavoriteRequestBodyEntity favoriteRequestBody,
            String sessionId) {
        return this.userApi.markAsFavorite(accountId, favoriteRequestBody, sessionId);
    }

    @Override
    public Observable<MoviesEntity> getFavoritesFromServer(int accountId, String sessionId, int page) {
        return this.userApi.getFavoriteMoviesFromServer(accountId, sessionId, page);
    }
}
