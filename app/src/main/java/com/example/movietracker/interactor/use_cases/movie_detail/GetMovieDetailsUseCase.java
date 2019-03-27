package com.example.movietracker.interactor.use_cases.movie_detail;

import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.interactor.UseCase;

import io.reactivex.Observable;

public class GetMovieDetailsUseCase extends UseCase<MovieDetailsEntity, Integer> {

    private final MovieRepository movieRepository;

    public GetMovieDetailsUseCase() {
        this.movieRepository = ClassProvider.movieRepository;
    }

    @Override
    protected Observable<MovieDetailsEntity> buildUseCaseObservable(Integer movieDetails) {
        return this.movieRepository.getMovieDetails(movieDetails);

    }
}