package com.example.movietracker.presenter;

import android.util.Log;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.view.contract.MovieDetailsView;

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
        this.disposable = new CompositeDisposable();
    }

    public void getMovieDetails(int movieId) {
        showLoading();
        disposable = this.movieInfoModel.getMovieInfo(movieId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new GetMovieDetailsObserver());
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
        if(!this.disposable.isDisposed()) {
            this.disposable.dispose();
        }
    }

    private class GetMovieDetailsObserver extends DisposableObserver<MovieDetailsEntity> {
        @Override
        public void onNext(MovieDetailsEntity movieDetailsEntity) {
            MovieDetailsPresenter.this.hideLoading();
            MovieDetailsPresenter.this.view.renderMovieDetails(movieDetailsEntity);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieDetailsPresenter.this.view.showToast(R.string.main_error);
            MovieDetailsPresenter.this.hideLoading();
            Log.e(TAG, e.getLocalizedMessage());
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "GetMovieDetailsObserver onComplete");
        }
    }
}

