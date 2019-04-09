package com.example.movietracker.data.repository;

import com.example.movietracker.data.database.MoviesDatabase;
import com.example.movietracker.data.entity.MovieFilter;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.net.RestClient;

import io.reactivex.Observable;

public interface MovieRepository {
    Observable<GenresEntity> getGenres();
    Observable<MoviesEntity> getMovies(MovieFilter movieFilter);
    Observable<MovieDetailsEntity> getMovieDetails(int movieId);
    Observable<MovieCastsEntity> getMovieCasts(int movieId);
    Observable<MovieVideosEntity> getMovieVideos(int movieId);
    Observable<MovieReviewsEntity> getMovieReviews(int movieId);
    void init(RestClient restClient, MoviesDatabase moviesDatabase);
}
