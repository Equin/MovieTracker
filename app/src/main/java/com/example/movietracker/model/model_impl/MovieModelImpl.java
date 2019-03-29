package com.example.movietracker.model.model_impl;

import com.example.movietracker.data.entity.MovieRequestEntity;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.interactor.use_cases.GetMoviesUseCase;
import com.example.movietracker.model.ModelContract;

public class MovieModelImpl implements ModelContract.MovieModel {

    private final GetMoviesUseCase getMoviesUseCase;

    public MovieModelImpl(){
        this.getMoviesUseCase = new GetMoviesUseCase();
    }

    @Override
    public void getMovies(DefaultObserver<MoviesEntity> defaultObserver, MovieRequestEntity movieRequestEntity) {
        this.getMoviesUseCase.execute(defaultObserver, movieRequestEntity);
    }

    @Override
    public void stop() {
        this.getMoviesUseCase.dispose();
    }
}
