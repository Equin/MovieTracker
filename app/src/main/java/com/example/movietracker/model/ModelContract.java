package com.example.movietracker.model;

import com.example.movietracker.view.model.Filters;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;

import io.reactivex.Observable;
import io.reactivex.Single;

public class ModelContract {

    public  interface GenreModel {
        Single<GenresEntity> getGenres();
    }

    public interface MovieModel {
        Observable<MoviesEntity> getMovies(Filters filters);

        Observable<MoviesEntity> getMovieListForPages(Filters filters);
    }

    public interface MovieInfoModel {
        Observable<MovieDetailsEntity> getMovieInfo(int movieId);
    }

    public interface MovieDetailTabsModel {
        Observable<MovieCastsEntity> getMovieCasts(int movieId);
        Observable<MovieReviewsEntity> getMovieReviews(int movieId);
        Observable<MovieVideosEntity> getMovieVideos(int movieId);
    }
}
