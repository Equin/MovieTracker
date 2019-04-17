package com.example.movietracker.model.model_impl;

import com.example.movietracker.view.model.Filters;
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
    public Observable<MoviesEntity> getMoviesWithFavorites(Filters filters) {
        return this.movieRepository.getMoviesWithFavorites(filters);
    }

    @Override
    public Observable<MoviesEntity> getMovieListForPagesWithFavorites(Filters filters) {
        return this.movieRepository.getMovieListForPagesWithFavorites(filters);
    }
}
