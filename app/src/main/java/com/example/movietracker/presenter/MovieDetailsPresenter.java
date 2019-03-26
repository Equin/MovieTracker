package com.example.movietracker.presenter;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.data.entity.MovieDetailsEntity;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.interactor.use_cases.GetGenresUseCase;
import com.example.movietracker.interactor.use_cases.GetMovieDetailsUseCase;
import com.example.movietracker.view.contract.MovieDetailsView;

import io.reactivex.annotations.NonNull;

public class MovieDetailsPresenter extends BasePresenter {

    private GetMovieDetailsUseCase getMovieDetailsUseCase;

    private MovieDetailsView view;

    public MovieDetailsPresenter() {
        this.getMovieDetailsUseCase = new GetMovieDetailsUseCase();
    }

    public void setView(MovieDetailsView movieDetailsView) {
        this.view = movieDetailsView;
    }

    public void initialize(int movieId) {
        showLoading();
        getMovieDetails(movieId);
    }

    private void getMovieDetails(int movieId) {
        this.getMovieDetailsUseCase.execute(new GetMovieDetailsObserver(), movieId);
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
        this.getMovieDetailsUseCase.dispose();
    }

    private class GetMovieDetailsObserver extends DefaultObserver<MovieDetailsEntity> {
        @Override
        public void onNext(MovieDetailsEntity movieDetailsEntity) {
            MovieDetailsPresenter.this.hideLoading();
            MovieDetailsPresenter.this.view.renderMovieDetails(movieDetailsEntity);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieDetailsPresenter.this.view.showToast(R.string.main_error);
            MovieDetailsPresenter.this.hideLoading();
        }
    }
}

