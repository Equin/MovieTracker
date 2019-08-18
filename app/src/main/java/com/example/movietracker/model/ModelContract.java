package com.example.movietracker.model;

import com.example.movietracker.data.entity.movie.MarkMovieAsFavoriteResultEntity;
import com.example.movietracker.data.entity.movie.MarkMovieAsFavoriteRequestBodyEntity;
import com.example.movietracker.data.entity.movie.MovieChangesEntity;
import com.example.movietracker.data.entity.user.UserDetailsEntity;
import com.example.movietracker.data.entity.session.SessionEntity;
import com.example.movietracker.data.entity.user.UserEntity;
import com.example.movietracker.data.entity.user.UserWithFavoriteMovies;
import com.example.movietracker.view.model.Filters;
import com.example.movietracker.data.entity.movie.MoviesEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class ModelContract {

    public  interface GenreModel {
        Single<GenresEntity> getGenres();
    }

    public interface MovieModel {
        Observable<MoviesEntity> getMoviesWithFavorites(Filters filters);
        Observable<MoviesEntity> getMovieListForPagesWithFavorites(Filters filters);
        Observable<List<Integer>> getMoviesIdList();
        Observable<MoviesEntity> getMoviesByTitle(Filters filters);
        Observable<MovieChangesEntity> getMoviesChanges();
    }

    public interface MovieInfoModel {
        Observable<MovieDetailsEntity> getMovieInfo(int movieId);
    }

    public interface MovieDetailTabsModel {
        Observable<MovieCastsEntity> getMovieCasts(int movieId);
        Observable<MovieReviewsEntity> getMovieReviews(int movieId);
        Observable<MovieVideosEntity> getMovieVideos(int movieId);
    }

    public interface UserModel {
        Observable<UserEntity> getUser();
        Observable<UserEntity> getUserWithFavorites();
        Single<UserDetailsEntity> getUserDetails(UserEntity userEntity);
        void addUser(UserEntity userEntity);
        Completable updateUser(UserEntity userEntity);
        Completable deleteUserFromFavorites(UserWithFavoriteMovies userWithFavoriteMovies);
        Completable syncFavoritesWithServer(UserEntity userEntity);
        Single<MarkMovieAsFavoriteResultEntity> markAsFavorite(int accountId, MarkMovieAsFavoriteRequestBodyEntity favoriteRequestBody, String sessionId);
    }

    public interface AuthModel {
        Single<SessionEntity> createSession();
        Single<SessionEntity> login(UserEntity userEntity);
        Single<SessionEntity> refreshSession(UserEntity userEntity);
        Completable invalidateSession(UserEntity userEntity);
    }
}
