package com.example.movietracker.interactor.genre;

import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.data.entity.MovieListEntity;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.interactor.UseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetMoviesUseCase extends UseCase<MovieListEntity, String> {

    private final MovieRepository movieRepository;

    @Inject
    public GetMoviesUseCase(
            MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    protected Observable<MovieListEntity> buildUseCaseObservable(String selectedGenres) {
        return this.movieRepository.getMovies(selectedGenres);

    }
}