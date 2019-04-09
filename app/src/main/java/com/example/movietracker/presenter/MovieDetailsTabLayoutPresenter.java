package com.example.movietracker.presenter;

import android.util.Log;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.model.model_impl.MovieDetailTabsModelImpl;
import com.example.movietracker.view.contract.TabLayoutView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsTabLayoutPresenter extends BasePresenter {

    private static final String TAG = MovieDetailsTabLayoutPresenter.class.getCanonicalName();

    private final ModelContract.MovieDetailTabsModel movieDetailTabsModel;

    private TabLayoutView view;

    private Disposable movieCastsDiaposable;
    private Disposable movieReviewsDiaposable;
    private Disposable movieVideosDiaposable;

    public MovieDetailsTabLayoutPresenter(TabLayoutView view) {
        this.movieDetailTabsModel = new MovieDetailTabsModelImpl();
        this.view = view;

        this.movieCastsDiaposable = new CompositeDisposable();
        this.movieReviewsDiaposable = new CompositeDisposable();
        this.movieVideosDiaposable = new CompositeDisposable();
    }

    public void getMovieCasts(int movieId) {
        showLoading();


        this.movieCastsDiaposable = this.movieDetailTabsModel.getMovieCasts(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new GetMovieCastObserver());

    }

    public void getMovieReviews(int movieId) {
        showLoading();

        this.movieReviewsDiaposable = this.movieDetailTabsModel.getMovieReviews(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new GetMovieReviewObserver());
    }

    public void getMovieVideos(int movieId) {
        showLoading();

        this.movieVideosDiaposable = this.movieDetailTabsModel.getMovieVideos(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new GetMovieVideosObserver());
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
        if (!this.movieCastsDiaposable.isDisposed()) {
            this.movieCastsDiaposable.dispose();
        }

        if (!this.movieReviewsDiaposable.isDisposed()) {
            this.movieReviewsDiaposable.dispose();
        }

        if (!this.movieVideosDiaposable.isDisposed()) {
            this.movieVideosDiaposable.dispose();
        }
    }

    private class GetMovieCastObserver extends DisposableObserver<MovieCastsEntity> {
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
            Log.e(TAG, e.getLocalizedMessage());
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "GetMovieCastObserver onComplete");
        }
    }

    private class GetMovieReviewObserver extends DisposableObserver<MovieReviewsEntity> {
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
            Log.e(TAG, e.getLocalizedMessage());
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "GetMovieReviewObserver onComplete");
        }
    }

    private class GetMovieVideosObserver extends DisposableObserver<MovieVideosEntity> {
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
            Log.e(TAG, e.getLocalizedMessage());
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "GetMovieVideosObserver onComplete");
        }
    }
}
