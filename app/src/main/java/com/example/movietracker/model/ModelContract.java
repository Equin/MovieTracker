package com.example.movietracker.model;

import com.example.movietracker.data.entity.MovieRequestEntity;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.interactor.DefaultObserver;

public class ModelContract {

    public  interface GenreModel {
        void getGenres(DefaultObserver<GenresEntity> defaultObserver);
        void stop();
    }

    public interface MovieModel {
        void getMovies(DefaultObserver<MoviesEntity> defaultObserver, MovieRequestEntity movieRequestEntity);
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
