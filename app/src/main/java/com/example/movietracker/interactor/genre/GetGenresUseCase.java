package com.example.movietracker.interactor.genre;

import com.example.movietracker.data.entity.GenreEntity;
import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.interactor.UseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetGenresUseCase extends UseCase<GenresEntity, Void> {

    private final MovieRepository movieRepository;

    @Inject
    public GetGenresUseCase(
            MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    protected Observable<GenresEntity> buildUseCaseObservable(Void aVoid) {
        return this.movieRepository.getGenres();

    }
}