package com.example.movietracker.data.repository;

import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.data.entity.MovieCastsEntity;
import com.example.movietracker.data.entity.MovieDetailsEntity;
import com.example.movietracker.data.entity.MovieReviewsEntity;
import com.example.movietracker.data.entity.MovieVideosEntity;
import com.example.movietracker.data.entity.MoviesEntity;

import io.reactivex.Observable;

public interface MovieRepository {
    Observable<GenresEntity> getGenres();
    Observable<MoviesEntity> getMovies(String genresId);
    Observable<MovieDetailsEntity> getMovieDetails(int movieId);
    Observable<MovieCastsEntity> getMovieCasts(int movieId);
    Observable<MovieVideosEntity> getMovieVideos(int movieId);
    Observable<MovieReviewsEntity> getMovieReviews(int movieId);
}
