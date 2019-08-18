package com.example.movietracker.model.model_impl;

import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.model.ModelContract;

import io.reactivex.Single;

public class GenreModelImpl implements ModelContract.GenreModel {

    private final MovieRepository movieRepository;

    public GenreModelImpl() {
        this.movieRepository = ClassProvider.movieRepository;
    }

    @Override
    public Single<GenresEntity> getGenres() {
        return this.movieRepository.getGenres();
    }
}
