package com.example.movietracker.interactor.use_cases.movie_detail;

import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.interactor.UseCase;

import javax.inject.Singleton;

import io.reactivex.Observable;

public class GetMovieCastsUseCase extends UseCase<MovieCastsEntity, Integer> {

    private final MovieRepository movieRepository;

    public GetMovieCastsUseCase() {
        this.movieRepository = ClassProvider.movieRepository;
    }

    @Override
    protected Observable<MovieCastsEntity> buildUseCaseObservable(Integer movieDetails) {
        return this.movieRepository.getMovieCasts(movieDetails);
    }
}