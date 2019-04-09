package com.example.movietracker.presenter;

import android.util.Log;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.Filters;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.view.contract.MovieListView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieListPresenter extends BasePresenter {

    private static final String TAG = MovieListPresenter.class.getCanonicalName();
    private MovieListView view;
    private ModelContract.MovieModel movieModel;
    private MoviesEntity moviesEntity;

    private Disposable movieDisposable;
    private Disposable moviePageDisposable;

    public MovieListPresenter(MovieListView view, ModelContract.MovieModel movieModel ) {
        this.view = view;
        this.movieModel = movieModel;
        this.moviesEntity = new MoviesEntity();
        this.movieDisposable = new CompositeDisposable();
        this.moviePageDisposable = new CompositeDisposable();
    }

    public void onMovieItemClicked(int itemPosition) {
        this.view.showMovieDetailScreen(
                this.moviesEntity.getMovies().get(itemPosition).getMovieId());
    }

    public void getMoviesByFilters(Filters filters) {
        getMovies(filters);
    }

    public void getLocalMovies() {
        this.view.renderMoviesList(this.moviesEntity);
    }

    public void getMoviesWithPagination(Filters filters) {
        getMoviesByPage(filters);
    }

    public MoviesEntity getMoviesEntity() {
        return this.moviesEntity;
    }

    private void getMovies(Filters filters) {
        showLoading();

        this.movieDisposable = this.movieModel.getMovies(filters)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new GetMoviesObserver());
    }

    private void getMoviesByPage(Filters filters) {
        showLoading();
        this.moviePageDisposable = this.movieModel.getMovies(filters)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new GetMoviesPageObserver());
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

        if (!this.moviePageDisposable.isDisposed()) {
            this.moviePageDisposable.dispose();
        }

        if (!this.movieDisposable.isDisposed()) {
            this.movieDisposable.dispose();
        }
    }

    private boolean isActionAllowed;

    public void lastMovieOfPageReached() {

        if(this.isActionAllowed) {
            this.isActionAllowed = false;
            if (this.moviesEntity.getTotalPages() > Filters.getInstance().getPage()) {
                Filters.getInstance().incrementPage();
                this.getMoviesWithPagination(Filters.getInstance());
            } else {
                showToast(R.string.movie_list_there_are_no_pages);
                this.isActionAllowed = true;
            }
        }
    }

    private void showToast(int stringResource) {
        this.view.showToast(stringResource);
    }

    private class GetMoviesObserver extends DisposableObserver<MoviesEntity> {
        @Override
        public void onNext(MoviesEntity moviesEntity) {
            MovieListPresenter.this.moviesEntity = moviesEntity;
            MovieListPresenter.this.view.renderMoviesList(moviesEntity);
            MovieListPresenter.this.isActionAllowed = true;
            MovieListPresenter.this.hideLoading();
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieListPresenter.this.view.showToast(R.string.main_error);
            MovieListPresenter.this.hideLoading();
            MovieListPresenter.this.isActionAllowed = true;
        }

        @Override
        public void onComplete() {

        }
    }

    private class GetMoviesPageObserver extends DisposableObserver<MoviesEntity> {
        @Override
        public void onNext(MoviesEntity moviesEntity) {
            MovieListPresenter.this.moviesEntity.setPage(moviesEntity.getPage());
            MovieListPresenter.this.moviesEntity.setTotalPages(moviesEntity.getTotalPages());
            MovieListPresenter.this.moviesEntity.addMovies(moviesEntity.getMovies());
            MovieListPresenter.this.view.renderAdditionalMovieListPage(MovieListPresenter.this.moviesEntity);
            MovieListPresenter.this.hideLoading();
            MovieListPresenter.this.isActionAllowed = true;
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieListPresenter.this.view.showToast(R.string.main_error);
            MovieListPresenter.this.hideLoading();
            MovieListPresenter.this.isActionAllowed = true;
            Log.e(TAG, e.getLocalizedMessage());
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "GetMoviesPageObserver onComplete");
        }
    }
}

