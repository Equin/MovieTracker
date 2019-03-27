package com.example.movietracker.interactor.use_cases.movie_detail;

import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
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