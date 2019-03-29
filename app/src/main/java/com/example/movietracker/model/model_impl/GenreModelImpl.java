package com.example.movietracker.model.model_impl;

import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.interactor.use_cases.GetGenresUseCase;
import com.example.movietracker.model.ModelContract;

public class GenreModelImpl implements ModelContract.GenreModel {

    private final GetGenresUseCase getGenresUseCase;

    public GenreModelImpl() {
        this.getGenresUseCase = new GetGenresUseCase();
    }

    @Override
    public void getGenres(DefaultObserver<GenresEntity> defaultObserver) {
        this.getGenresUseCase.execute(defaultObserver, null);
    }

    @Override
    public void stop() {
        this.getGenresUseCase.dispose();
    }
}
