package com.example.movietracker.interactor.use_cases;

import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.interactor.UseCase;

import io.reactivex.Observable;

public class GetGenresUseCase extends UseCase<GenresEntity, Void> {

    private final MovieRepository movieRepository;

    public GetGenresUseCase() {
        this.movieRepository = ClassProvider.movieRepository;
    }

    @Override
    protected Observable<GenresEntity> buildUseCaseObservable(Void aVoid) {
        return this.movieRepository.getGenres();
    }
}