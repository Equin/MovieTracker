package com.example.movietracker.interactor.use_cases;

import com.example.movietracker.data.entity.MovieDetailsEntity;
import com.example.movietracker.data.entity.MovieReviewsEntity;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.interactor.UseCase;

import io.reactivex.Observable;

public class GetMovieReviewsUseCase extends UseCase<MovieReviewsEntity, Integer> {

    private final MovieRepository movieRepository;

    public GetMovieReviewsUseCase() {
        this.movieRepository = ClassProvider.movieRepository;
    }

    @Override
    protected Observable<MovieReviewsEntity> buildUseCaseObservable(Integer movieDetails) {
        return this.movieRepository.getMovieReviews(movieDetails);

    }
}