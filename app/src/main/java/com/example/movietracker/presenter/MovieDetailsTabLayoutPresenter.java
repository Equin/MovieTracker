package com.example.movietracker.presenter;

import android.util.Log;

import com.example.movietracker.AndroidApplication;
import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.model.model_impl.MovieDetailTabsModelImpl;
import com.example.movietracker.view.contract.TabLayoutView;
import com.example.movietracker.view.helper.ImageSaveUtility;
import com.example.movietracker.view.helper.RxDisposeHelper;

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

    private Disposable movieCastsDisposable;
    private Disposable movieReviewsDisposable;
    private Disposable movieVideosDisposable;

    public MovieDetailsTabLayoutPresenter(TabLayoutView view) {
        this.movieDetailTabsModel = new MovieDetailTabsModelImpl();
        this.view = view;
    }

    /**
     * getting movie cast by movieId
     *
     * @param movieId
     */
    public void getMovieCasts(int movieId) {
        showLoading();

        this.movieCastsDisposable = this.movieDetailTabsModel.getMovieCasts(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new GetMovieCastObserver());
    }

    /**
     * getting movie reviews by movieId
     *
     * @param movieId
     */
    public void getMovieReviews(int movieId) {
        showLoading();

        this.movieReviewsDisposable = this.movieDetailTabsModel.getMovieReviews(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new GetMovieReviewObserver());
    }

    /**
     * getting movie videos by movieId
     * @param movieId
     */
    public void getMovieVideos(int movieId) {
        showLoading();

        this.movieVideosDisposable = this.movieDetailTabsModel.getMovieVideos(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new GetMovieVideosObserver());
    }

    /**
     * saving clicked image to disc
     * @param imageName - name of image
     * @param imageSourcePath - url of image
     */
    public void onImageViewLongClick(String imageName, String imageSourcePath) {
        ImageSaveUtility.saveImageToDisk(AndroidApplication.getRunningActivity(), imageSourcePath, imageName, view);
    }

    private void showLoading() {
        if (this.view != null) {
            this.view.showLoading();
        }
    }

    private void hideLoading() {
        if (this.view != null) {
            this.view.hideLoading();
        }
    }

    private void displayNothingToShow() {
        if (this.view != null) {
            this.view.displayNothingToShowHint();
        }
    }

    private void showToast(int resourceId) {
        if (this.view != null) {
            this.view.showToast(resourceId);
        }
    }

    @Override
    public void destroy() {
        this.view = null;
        RxDisposeHelper.dispose(this.movieCastsDisposable);
        RxDisposeHelper.dispose(this.movieReviewsDisposable);
        RxDisposeHelper.dispose(this.movieVideosDisposable);
    }

    /**
     * getting movie casts
     * onNext: remdering movie casts to casts fragment tab
     * onError: diplaying nothing to show hint
     */
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
            MovieDetailsTabLayoutPresenter.this.showToast(R.string.main_error);
            MovieDetailsTabLayoutPresenter.this.hideLoading();
            MovieDetailsTabLayoutPresenter.this.displayNothingToShow();
            Log.e(TAG, e.getLocalizedMessage());
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "GetMovieCastObserver onComplete");
        }
    }

    /**
     * getting movie reviews
     * onNext: rendering movie review to reviews fragment tab
     * onError: diplaying nothing to show hint
     */
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
            MovieDetailsTabLayoutPresenter.this.showToast(R.string.main_error);
            MovieDetailsTabLayoutPresenter.this.hideLoading();
            MovieDetailsTabLayoutPresenter.this.displayNothingToShow();
            Log.e(TAG, e.getLocalizedMessage());
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "GetMovieReviewObserver onComplete");
        }
    }

    /**
     * getting movie videos
     * onNext: rendering movie videos to videos fragment tab
     * onError: diplaying nothing to show hint
     */
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
            MovieDetailsTabLayoutPresenter.this.showToast(R.string.main_error);
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
