package com.example.movietracker.interactor.use_cases;

import com.example.movietracker.data.entity.MovieFilter;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.interactor.UseCase;

import io.reactivex.Observable;

public class GetMoviesUseCase extends UseCase<MoviesEntity, MovieFilter> {

    private final MovieRepository movieRepository;

    public GetMoviesUseCase() {
        this.movieRepository = ClassProvider.movieRepository;
    }

    @Override
    protected Observable<MoviesEntity> buildUseCaseObservable(MovieFilter movieFilter) {
        return this.movieRepository.getMovies(movieFilter);
    }
}