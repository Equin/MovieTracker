package com.example.movietracker.presenter;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.model.model_impl.MovieDetailTabsModelImpl;
import com.example.movietracker.view.contract.TabLayoutView;

import io.reactivex.annotations.NonNull;

public class MovieDetailsTabLayoutPresenter extends BasePresenter {

    private final ModelContract.MovieDetailTabsModel movieDetailTabsModel;

    private TabLayoutView view;

    public MovieDetailsTabLayoutPresenter(TabLayoutView view) {
        this.movieDetailTabsModel = new MovieDetailTabsModelImpl();
        this.view = view;
    }

    public void getMovieCasts(int movieId) {
        showLoading();
        this.movieDetailTabsModel.getMovieCasts(new GetMovieCastObserver(), movieId);
    }

    public void getMovieReviews(int movieId) {
        showLoading();
        this.movieDetailTabsModel.getMovieReviews(new GetMovieReviewObserver(), movieId);
    }

    public void getMovieVideos(int movieId) {
        showLoading();
        this.movieDetailTabsModel.getMovieVideos(new GetMovieVideosObserver(), movieId);
    }

    private void showLoading() {
        this.view.showLoading();
    }

    private void hideLoading() {
        this.view.hideLoading();
    }

    private void displayNothingToShow() {
        this.view.displayNothingToShowHint();
    }

    @Override
    public void destroy() {
        this.view = null;
        this.movieDetailTabsModel.stop();
    }

    private class GetMovieCastObserver extends DefaultObserver<MovieCastsEntity> {
        @Override
        public void onNext(MovieCastsEntity movieCastsEntity) {
            MovieDetailsTabLayoutPresenter.this.hideLoading();

            if (movieCastsEntity.getMovieCasts().isEmpty()) {
                MovieDetailsTabLayoutPresenter.this.displayNothingToShow();
            } else {
                MovieDetailsTabLayoutPresenter.this.view.renderInfoToTab(movieCastsEntity);
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieDetailsTabLayoutPresenter.this.view.showToast(R.string.main_error);
            MovieDetailsTabLayoutPresenter.this.hideLoading();
            MovieDetailsTabLayoutPresenter.this.displayNothingToShow();
        }
    }

    private class GetMovieReviewObserver extends DefaultObserver<MovieReviewsEntity> {
        @Override
        public void onNext(MovieReviewsEntity movieReviewsEntity) {
            MovieDetailsTabLayoutPresenter.this.hideLoading();

            if (movieReviewsEntity.getReviews().isEmpty()) {
                MovieDetailsTabLayoutPresenter.this.displayNothingToShow();
            } else {
                MovieDetailsTabLayoutPresenter.this.view.renderInfoToTab(movieReviewsEntity);
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieDetailsTabLayoutPresenter.this.view.showToast(R.string.main_error);
            MovieDetailsTabLayoutPresenter.this.hideLoading();
            MovieDetailsTabLayoutPresenter.this.displayNothingToShow();
        }
    }

    private class GetMovieVideosObserver extends DefaultObserver<MovieVideosEntity> {
        @Override
        public void onNext(MovieVideosEntity movieVideosEntity) {
            MovieDetailsTabLayoutPresenter.this.hideLoading();

            if (movieVideosEntity.getMovieVideoResultEntities().isEmpty()) {
                MovieDetailsTabLayoutPresenter.this.displayNothingToShow();
            } else {
                MovieDetailsTabLayoutPresenter.this.view.renderInfoToTab(movieVideosEntity);
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieDetailsTabLayoutPresenter.this.view.showToast(R.string.main_error);
            MovieDetailsTabLayoutPresenter.this.hideLoading();
            MovieDetailsTabLayoutPresenter.this.displayNothingToShow();
        }
    }
}
