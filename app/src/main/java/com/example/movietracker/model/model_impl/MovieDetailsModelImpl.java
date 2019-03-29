package com.example.movietracker.model.model_impl;

import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.interactor.use_cases.movie_detail.GetMovieDetailsUseCase;
import com.example.movietracker.model.ModelContract;

public class MovieDetailsModelImpl implements ModelContract.MovieDetailsModel {

    private final GetMovieDetailsUseCase getMovieDetailsUseCase;

    public MovieDetailsModelImpl() {
        this.getMovieDetailsUseCase = new GetMovieDetailsUseCase();
    }

    @Override
    public void getMovieDetails(DefaultObserver<MovieDetailsEntity> defaultObserver, int movieId) {
        this.getMovieDetailsUseCase.execute(defaultObserver, movieId);
    }

    @Override
    public void stop() {
        this.getMovieDetailsUseCase.dispose();
    }
}
