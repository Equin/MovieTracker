package com.example.movietracker.interactor.use_cases;

import com.example.movietracker.data.entity.MovieDetailsEntity;
import com.example.movietracker.data.entity.MovieVideosEntity;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.interactor.UseCase;

import io.reactivex.Observable;

public class GetMovieVideosUseCase extends UseCase<MovieVideosEntity, Integer> {

    private final MovieRepository movieRepository;

    public GetMovieVideosUseCase() {
        this.movieRepository = ClassProvider.movieRepository;
    }

    @Override
    protected Observable<MovieVideosEntity> buildUseCaseObservable(Integer movieDetails) {
        return this.movieRepository.getMovieVideos(movieDetails);

    }
}