package com.example.movietracker.model.model_impl;

import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.model.ModelContract;

import io.reactivex.Observable;

public class MovieInfoModelImpl implements ModelContract.MovieInfoModel {

    private final MovieRepository movieRepository;

    public MovieInfoModelImpl() {
        this.movieRepository = ClassProvider.movieRepository;
    }

    @Override
    public Observable<MovieDetailsEntity> getMovieInfo(int movieId) {
        return this.movieRepository.getMovieDetails(movieId);
    }
}
