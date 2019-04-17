package com.example.movietracker.presenter;

import android.util.Log;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.view.contract.MovieDetailsView;
import com.example.movietracker.view.helper.RxDisposeHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsPresenter extends BasePresenter {

    private static final String TAG = MovieDetailsPresenter.class.getCanonicalName();
    private final ModelContract.MovieInfoModel movieInfoModel;

    private MovieDetailsView view;
    private Disposable disposable;

    public MovieDetailsPresenter(MovieDetailsView movieDetailsView, ModelContract.MovieInfoModel movieInfoModel) {
        this.movieInfoModel = movieInfoModel;
        this.view = movieDetailsView;
    }

    public void getMovieDetails(int movieId) {
        showLoading();
        disposable = this.movieInfoModel.getMovieInfo(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new GetMovieDetailObserver());
    }

    private void showLoading() {
        if(view != null) {
            this.view.showLoading();
        }
    }

    private void hideLoading() {
        if(view != null) {
            this.view.hideLoading();
        }
    }

    private void showToast(int resourceId) {
        if(view != null) {
            this.view.showToast(resourceId);
        }
    }

    private void renderMovieDetails(MovieDetailsEntity movieDetailsEntity) {
        if (this.view != null) {
            this.view.renderMovieDetails(movieDetailsEntity);
        }
    }

    private void displayNothingToShow() {
        if (this.view != null) {
            this.view.displayNothingToShowHint();
        }
    }

    @Override
    public void destroy() {
        this.view = null;
        RxDisposeHelper.dispose(this.disposable);
    }

    private class GetMovieDetailObserver extends DisposableObserver<MovieDetailsEntity> {
        @Override
        public void onNext(MovieDetailsEntity movieDetailsEntity) {
            MovieDetailsPresenter.this.hideLoading();
            if (movieDetailsEntity == null || movieDetailsEntity.getMovieReleaseDate() == null) {
                displayNothingToShow();
                return;
            }

            MovieDetailsPresenter.this.renderMovieDetails(movieDetailsEntity);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieDetailsPresenter.this.showToast(R.string.main_error);
            MovieDetailsPresenter.this.hideLoading();
           Log.e(TAG, e.getLocalizedMessage());
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "GetMovieDetailsObserver onComplete");
        }
    };
}

