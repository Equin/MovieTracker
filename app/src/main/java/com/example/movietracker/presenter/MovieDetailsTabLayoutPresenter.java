package com.example.movietracker.presenter;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieCastsEntity;
import com.example.movietracker.data.entity.MovieReviewsEntity;
import com.example.movietracker.data.entity.MovieVideosEntity;
import com.example.movietracker.data.entity.TabEntities;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.interactor.use_cases.GetMovieCastsUseCase;
import com.example.movietracker.interactor.use_cases.GetMovieReviewsUseCase;
import com.example.movietracker.interactor.use_cases.GetMovieVideosUseCase;
import com.example.movietracker.view.contract.TabLayoutView;

import io.reactivex.annotations.NonNull;

public class MovieDetailsTabLayoutPresenter extends BasePresenter {

    private GetMovieCastsUseCase getMovieCastsUseCase;
    private GetMovieReviewsUseCase getMovieReviewsUseCase;
    private GetMovieVideosUseCase getMovieVideosUseCase;

    private TabLayoutView view;

    public MovieDetailsTabLayoutPresenter() {
        this.getMovieCastsUseCase = new GetMovieCastsUseCase();
        this.getMovieReviewsUseCase = new GetMovieReviewsUseCase();
        this.getMovieVideosUseCase = new GetMovieVideosUseCase();
    }

    public void setView(TabLayoutView view) {
        this.view = view;
    }

    public void initialize() {
        showLoading();
    }

    public void getMovieCasts(int movieId) {
        this.getMovieCastsUseCase.execute(new GetMovieCastObserver(), movieId);
    }

    public void getMovieReviews(int movieId) {
        this.getMovieReviewsUseCase.execute(new GetMovieReviewObserver(), movieId);
    }

    public void getMovieVideos(int movieId) {
        this.getMovieVideosUseCase.execute(new GetMovieVideosObserver(), movieId);
    }

    private void showLoading() {
        this.view.showLoading();
    }

    private void hideLoading() {
        this.view.hideLoading();
    }

    @Override
    public void destroy() {
        this.view = null;
        this.getMovieCastsUseCase.dispose();
    }

    private class GetMovieCastObserver extends DefaultObserver<MovieCastsEntity> {
        @Override
        public void onNext(MovieCastsEntity movieCastsEntity) {
            MovieDetailsTabLayoutPresenter.this.hideLoading();
            MovieDetailsTabLayoutPresenter.this.view.renderInfoToTab(movieCastsEntity);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieDetailsTabLayoutPresenter.this.view.showToast(R.string.main_error);
            MovieDetailsTabLayoutPresenter.this.hideLoading();
        }
    }

    private class GetMovieReviewObserver extends DefaultObserver<MovieReviewsEntity> {
        @Override
        public void onNext(MovieReviewsEntity movieReviewsEntity) {
            MovieDetailsTabLayoutPresenter.this.hideLoading();
            MovieDetailsTabLayoutPresenter.this.view.renderInfoToTab(movieReviewsEntity);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieDetailsTabLayoutPresenter.this.view.showToast(R.string.main_error);
            MovieDetailsTabLayoutPresenter.this.hideLoading();
        }
    }

    private class GetMovieVideosObserver extends DefaultObserver<MovieVideosEntity> {
        @Override
        public void onNext(MovieVideosEntity movieVideosEntity) {
            MovieDetailsTabLayoutPresenter.this.hideLoading();
            MovieDetailsTabLayoutPresenter.this.view.renderInfoToTab(movieVideosEntity);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieDetailsTabLayoutPresenter.this.view.showToast(R.string.main_error);
            MovieDetailsTabLayoutPresenter.this.hideLoading();
        }
    }
}
