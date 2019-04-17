package com.example.movietracker.model;

import com.example.movietracker.data.entity.UserEntity;
import com.example.movietracker.data.entity.UserWithFavoriteMovies;
import com.example.movietracker.view.model.Filters;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;

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
        void addUser(UserEntity userEntity);
        Completable updateUser(UserEntity userEntity);
        Completable deleteUserFromFavorites(UserWithFavoriteMovies userWithFavoriteMovies);
    }
}
