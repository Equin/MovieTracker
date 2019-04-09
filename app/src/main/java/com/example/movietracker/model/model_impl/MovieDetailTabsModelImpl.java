package com.example.movietracker.model.model_impl;

import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.model.ModelContract;

import io.reactivex.Observable;

public class MovieDetailTabsModelImpl implements ModelContract.MovieDetailTabsModel {

    private final MovieRepository movieRepository;

    public MovieDetailTabsModelImpl() {
        this.movieRepository = ClassProvider.movieRepository;
    }

    @Override
    public Observable<MovieCastsEntity> getMovieCasts(int movieId) {
        return this.movieRepository.getMovieCasts(movieId);
    }

    @Override
    public Observable<MovieReviewsEntity> getMovieReviews(int movieId) {
        return this.movieRepository.getMovieReviews(movieId);
    }

    @Override
    public Observable<MovieVideosEntity> getMovieVideos(int movieId) {
        return this.movieRepository.getMovieVideos(movieId);
    }
}
