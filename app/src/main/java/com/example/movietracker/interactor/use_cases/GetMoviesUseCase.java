package com.example.movietracker.interactor.use_cases;

import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.interactor.UseCase;

import io.reactivex.Observable;

public class GetMoviesUseCase extends UseCase<MoviesEntity, String> {

    private final MovieRepository movieRepository;

    public GetMoviesUseCase() {
        this.movieRepository = ClassProvider.movieRepository;
    }

    @Override
    protected Observable<MoviesEntity> buildUseCaseObservable(String selectedGenres) {
        return this.movieRepository.getMovies(selectedGenres);

    }
}