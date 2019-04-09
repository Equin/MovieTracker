package com.example.movietracker.model;

import com.example.movietracker.data.entity.MovieFilter;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.interactor.DefaultObserver;

import io.reactivex.Observable;
import io.reactivex.Single;

public class ModelContract {

    public  interface GenreModel {
        Single<GenresEntity> getGenres();
    }

    public interface MovieModel {
        void getMovies(DefaultObserver<MoviesEntity> defaultObserver, MovieFilter movieFilter);
        void stop();
    }

    public interface MovieInfoModel {
        void getMovieInfo(DefaultObserver<MovieDetailsEntity> defaultObserver, int movieId);
        void stop();
    }

    public interface MovieDetailTabsModel {
        void getMovieCasts(DefaultObserver<MovieCastsEntity> defaultObserver, int movieId);
        void getMovieReviews(DefaultObserver<MovieReviewsEntity> defaultObserver, int movieId);
        void getMovieVideos(DefaultObserver<MovieVideosEntity> defaultObserver, int movieId);
        void stop();
    }
}
