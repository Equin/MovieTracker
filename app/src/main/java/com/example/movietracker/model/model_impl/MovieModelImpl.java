package com.example.movietracker.model.model_impl;

import com.example.movietracker.data.entity.Filters;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.model.ModelContract;

import io.reactivex.Observable;

public class MovieModelImpl implements ModelContract.MovieModel {

    private final MovieRepository movieRepository;

    public MovieModelImpl() {
        this.movieRepository = ClassProvider.movieRepository;
    }

    @Override
    public Observable<MoviesEntity> getMovies(Filters filters) {
        return this.movieRepository.getMovies(filters);
    }
}
