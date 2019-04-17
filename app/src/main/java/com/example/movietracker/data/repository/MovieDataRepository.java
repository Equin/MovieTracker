package com.example.movietracker.data.repository;

import com.example.movietracker.data.database.MoviesDatabase;
import com.example.movietracker.data.database.dao.GenresDao;
import com.example.movietracker.data.database.dao.MovieDao;
import com.example.movietracker.data.database.dao.MovieDetailDao;
import com.example.movietracker.data.database.dao.UserDao;
import com.example.movietracker.data.entity.MovieResultEntity;
import com.example.movietracker.data.entity.UserEntity;
import com.example.movietracker.data.entity.UserWithFavoriteMovies;
import com.example.movietracker.view.model.Filters;
import com.example.movietracker.data.entity.entity_mapper.MovieCastsDataMapper;
import com.example.movietracker.data.entity.entity_mapper.MovieReviewsDataMapper;
import com.example.movietracker.data.entity.entity_mapper.MovieVideosDataMapper;
import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.net.RestClient;
import com.example.movietracker.data.net.api.MovieApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MovieDataRepository implements MovieRepository {

    private static final int MOVIE_PER_PAGE = 20;
    private static final int DB_REQUEST_TIMEOUT = 2;


    private MovieApi movieApi;
    private GenresDao genresDao;
    private MovieDao movieDao;
    private MovieDetailDao movieDetailDao;
    private UserDao userDao;

    private MovieVideosDataMapper movieVideosDataMapper;
    private MovieCastsDataMapper movieCastsDataMapper;
    private MovieReviewsDataMapper movieReviewsDataMapper;

    private MovieDataRepository() {}

    private static class SingletonHelper {
        private static final MovieDataRepository INSTANCE = new MovieDataRepository();
    }

    public static MovieDataRepository getInstance(){
        return SingletonHelper.INSTANCE;
    }

    @Override
    public void init(RestClient restClient, MoviesDatabase moviesDatabase) {
        this.movieApi = restClient.getMovieApi();
        this.genresDao = moviesDatabase.getGenresDao();
        this.movieDao = moviesDatabase.getMovieDao();
        this.userDao = moviesDatabase.getUserDao();
        this.movieDetailDao = moviesDatabase.getMovieDetailDao();
        this.movieCastsDataMapper = new MovieCastsDataMapper();
        this.movieVideosDataMapper = new MovieVideosDataMapper();
        this.movieReviewsDataMapper = new MovieReviewsDataMapper();
    }

    @Override
    public Single<GenresEntity> getGenres() {
        return this.movieApi.getGenres()
                .doOnSuccess(genresEntity -> this.genresDao.saveGenres(genresEntity.getGenres()))
                .onErrorResumeNext(this.genresDao.getAllGenres().map(GenresEntity::new));
    }

    @Override
    public Single<GenresEntity> getLocalGenres() {
        return this.genresDao.getAllGenres().map(genreEntities -> new GenresEntity())
                .onErrorResumeNext(this.movieApi.getGenres()
                        .doOnSuccess(genresEntity -> this.genresDao.saveGenres(genresEntity.getGenres())));
    }

    @Override
    public Observable<MoviesEntity> getMovies(Filters filters) {
        return this.movieApi.getMovies(
                filters.getCommaSeparatedGenres(),
                filters.getSortBy()
                        .concat(".").concat(
                        filters.getOrder().name().toLowerCase()),
                filters.getPage(),
                filters.isIncludeAdult())
                .doOnNext(moviesEntity -> {
                    this.movieDao.saveMovies(moviesEntity.getMovies());
                    this.movieDao.addMovieGenreRelation(moviesEntity.getMovies());
                })
                .onExceptionResumeNext(
                        this.movieDao.getMovies(
                                filters.getSelectedGenresIds(),
                                MOVIE_PER_PAGE,
                                (filters.getPage()-1)*MOVIE_PER_PAGE,
                                filters.isIncludeAdult()
                        )
                                .map(movieResultEntities -> {
                                    int totalPage = Math.round((float)this.movieDao.getTotalResults(
                                            filters.getSelectedGenresIds(),
                                            filters.isIncludeAdult()) / MOVIE_PER_PAGE);

                                    return new MoviesEntity(
                                            filters.getPage(),
                                            totalPage,
                                            movieResultEntities
                                    );
                                }
                        )
                );
    }

    @Override
    public Observable<MoviesEntity> getMoviesWithFavorites(Filters filters) {
        return  getMovieFavorites(getMovies(filters));
    }

    private Observable<MoviesEntity> getMovieFavorites( Observable<MoviesEntity> moviesEntityObservable ) {
        return Observable.zip(
                getUserWithFavorites().subscribeOn(Schedulers.newThread()),
                moviesEntityObservable.subscribeOn(Schedulers.newThread()),
                (userEntity, moviesEntity) -> {

                    if (userEntity.getFavoriteMovies() == null || userEntity.getFavoriteMovies().isEmpty()) {
                        return moviesEntity;
                    }

                    for (MovieResultEntity movieResultEntity : moviesEntity.getMovies()) {
                        boolean isFavorite = false;
                        for (MovieResultEntity userFavMovies : userEntity.getFavoriteMovies()) {
                            if(movieResultEntity.getMovieId() == userFavMovies.getMovieId()) {
                                isFavorite = true;
                            }
                        }
                        movieResultEntity.setFavorite(isFavorite);
                    }
                    return moviesEntity;
                });
    }

    @Override
    public Observable<MoviesEntity> getMovieListForPagesWithFavorites(Filters filters) {
       return  getMovieFavorites(getMovieListForPages(filters));
    }

    @Override
    public Observable<MoviesEntity> getMovieListForPages(Filters filters) {
        List<Observable<MoviesEntity>> requests = new ArrayList<>();

        for (int i = 1; i <= filters.getPage(); i++) {
            requests.add(addRequest(filters, i));
        }

        return Observable.zip(requests, movies -> {
            MoviesEntity moviesEntity = new MoviesEntity();

            for (Object movie : movies) {
                moviesEntity.setPage(((MoviesEntity) movie).getPage());
                moviesEntity.setTotalPages(((MoviesEntity) movie).getTotalPages());

                //removing duplicates of movie pages
                if (moviesEntity.getMovies().isEmpty()
                        || (!moviesEntity.getMovies().isEmpty()
                        && ((MoviesEntity) movie).getMovies().get(0).getMovieId()
                        != moviesEntity.getMovies().get(0).getMovieId())) {
                    moviesEntity.addMovies(((MoviesEntity) movie).getMovies());
                }
            }

            return moviesEntity;
        });
    }

    private Observable<MoviesEntity> addRequest(Filters filters, int page) {
        return this.movieApi.getMovies(
                filters.getCommaSeparatedGenres(),
                filters.getSortBy()
                        .concat(".").concat(
                        filters.getOrder().name().toLowerCase()),
                page,
                filters.isIncludeAdult())
                .doOnNext(moviesEntity -> {
                    this.movieDao.saveMovies(moviesEntity.getMovies());
                    this.movieDao.addMovieGenreRelation(moviesEntity.getMovies());
                })
                .onExceptionResumeNext(
                        this.movieDao.getMovies(
                                filters.getSelectedGenresIds(),
                                filters.getPage() * MOVIE_PER_PAGE,
                                0,
                                filters.isIncludeAdult()
                        )
                                .map(movieResultEntities -> {
                                    int totalPage = Math.round((float)this.movieDao.getTotalResults(
                                            filters.getSelectedGenresIds(),
                                            filters.isIncludeAdult()) / MOVIE_PER_PAGE);

                                      return new MoviesEntity(
                                              filters.getPage(),
                                                totalPage,
                                                movieResultEntities
                                        );
                                })
                );
    }

    @Override
    public Observable<MovieDetailsEntity> getMovieDetails(int movieId) {
        return this.movieApi.getMovieDetailsById(movieId)
                .doOnNext(movieDetailsEntity ->
                        movieDetailDao.saveInfo(movieDetailsEntity)
                )
                .onExceptionResumeNext(
                        movieDetailDao.getMovieInfo(movieId)
                                .map(movieDetailsEntity -> {
                                    if (!movieDetailsEntity.isEmpty()) {
                                        List<GenreEntity> genreEntities = movieDetailDao.getGenres(movieId).blockingFirst();
                                        movieDetailsEntity.get(0).setGenres(genreEntities);
                                        return movieDetailsEntity.get(0);
                                    }
                                    else  {
                                        return new MovieDetailsEntity();
                                    }
                        }));
    }

    @Override
    public Observable<MovieCastsEntity> getMovieCasts(int movieId) {
        return this.movieApi.getMovieCasts(movieId)
                .doOnNext(movieCastsEntity -> this.movieDetailDao.saveCasts(
                        this.movieCastsDataMapper.transformToList(movieCastsEntity))
                ).onExceptionResumeNext(
                        this.movieDetailDao.getCasts(movieId)
                                .map(this.movieCastsDataMapper::transformFromList)
                );
    }

    @Override
    public Observable<MovieVideosEntity> getMovieVideos(int movieId) {
        return this.movieApi.getMovieVideos(movieId)
                .doOnNext(movieVideosEntity -> this.movieDetailDao.saveMovieVideos(
                        this.movieVideosDataMapper.transformToList(movieVideosEntity))
                ).onExceptionResumeNext(
                        this.movieDetailDao.getMovieVideos(movieId)
                                .map(this.movieVideosDataMapper::transformFromList)
                );
    }

    @Override
    public Observable<MovieReviewsEntity> getMovieReviews(int movieId) {
        return this.movieApi.getMovieReviews(movieId)
                .doOnNext(movieReviewsEntity -> this.movieDetailDao.saveMovieReviews(
                        this.movieReviewsDataMapper.transformToList(movieReviewsEntity))
                ).onExceptionResumeNext(
                        this.movieDetailDao.getMovieReviews(movieId)
                                .map(this.movieReviewsDataMapper::transformFromList)
                );
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
}
