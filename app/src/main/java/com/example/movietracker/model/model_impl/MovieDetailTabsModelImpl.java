package com.example.movietracker.model.model_impl;

import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.interactor.use_cases.movie_detail.GetMovieCastsUseCase;
import com.example.movietracker.interactor.use_cases.movie_detail.GetMovieDetailsUseCase;
import com.example.movietracker.interactor.use_cases.movie_detail.GetMovieReviewsUseCase;
import com.example.movietracker.interactor.use_cases.movie_detail.GetMovieVideosUseCase;
import com.example.movietracker.model.ModelContract;

public class MovieDetailTabsModelImpl implements ModelContract.MovieDetailTabsModel {

    private final GetMovieReviewsUseCase getMovieReviewsUseCase;
    private final GetMovieVideosUseCase getMovieVideosUseCase;
    private final GetMovieCastsUseCase getMovieCastsUseCase;

    public MovieDetailTabsModelImpl() {
        this.getMovieCastsUseCase = new GetMovieCastsUseCase();
        this.getMovieReviewsUseCase = new GetMovieReviewsUseCase();
        this.getMovieVideosUseCase = new GetMovieVideosUseCase();
    }

    @Override
    public void getMovieCasts(DefaultObserver<MovieCastsEntity> defaultObserver, int movieId) {
        this.getMovieCastsUseCase.execute(defaultObserver, movieId);
    }

    @Override
    public void getMovieReviews(DefaultObserver<MovieReviewsEntity> defaultObserver, int movieId) {
        this.getMovieReviewsUseCase.execute(defaultObserver, movieId);
    }

    @Override
    public void getMovieVideos(DefaultObserver<MovieVideosEntity> defaultObserver, int movieId) {
        this.getMovieVideosUseCase.execute(defaultObserver, movieId);
    }

    @Override
    public void stop() {
        this.getMovieCastsUseCase.dispose();
        this.getMovieReviewsUseCase.dispose();
        this.getMovieVideosUseCase.dispose();
    }
}
