package com.example.movietracker.presenter;

import android.util.Log;

import com.example.movietracker.R;
import com.example.movietracker.view.model.Filters;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.view.contract.MovieListView;
import com.example.movietracker.view.model.MovieRecyclerItemPosition;

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
    private MovieRecyclerItemPosition recyclerItemPosition;

    private boolean isActionAllowed;

    private Disposable movieDisposable;
    private Disposable moviePageDisposable;
    private Disposable movieListPagesDisposable;


    public MovieListPresenter(
            MovieListView view,
            ModelContract.MovieModel movieModel,
            MovieRecyclerItemPosition recyclerItemPosition) {
        this.view = view;
        this.movieModel = movieModel;
        this.moviesEntity = new MoviesEntity();
        this.movieDisposable = new CompositeDisposable();
        this.moviePageDisposable = new CompositeDisposable();
        this.movieListPagesDisposable = new CompositeDisposable();
        this.recyclerItemPosition = recyclerItemPosition;
    }

    public void onMovieItemClicked(int itemPosition, int itemOffset) {
        int movieId = this.moviesEntity.getMovies().get(itemPosition).getMovieId();
        this.view.showMovieDetailScreen(movieId);
        this.setRecyclerItemPosition(movieId, itemOffset);
    }

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

    public void getMoviesByFilters(Filters filters) {

        if(this.recyclerItemPosition.getMovieId() == 0) {
            getMovies(filters);
        } else {
            getMovieListForAllPages(filters);
        }
    }

    private void setRecyclerItemPosition(int movieId, int offset) {
        this.recyclerItemPosition.setMovieId(movieId);
        this.recyclerItemPosition.setOffset(offset);
    }

    private void scrollToMovieWithId(int itemPosition, int itemOffset) {
        this.view.scrollToMovie(itemPosition, itemOffset);
    }

    private void scrollToMovieIfPossible(MoviesEntity moviesEntity) {
        for(int i = 0; i < moviesEntity.getMovies().size(); i++) {
            if (moviesEntity.getMovies().get(i).getMovieId() == recyclerItemPosition.getMovieId()) {
                this.scrollToMovieWithId(i, recyclerItemPosition.getOffset());
            }
        }
    }

    private void getMoviesWithPagination(Filters filters) {
        getMoviesByPage(filters);
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

    private void getMovieListForAllPages(Filters filters) {
        showLoading();

        this.movieListPagesDisposable = this.movieModel.getMovieListForPages(filters)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new GetMovieListForPagesObserver());
    }

    private void showLoading() {
        this.view.showLoading();
    }

    private void hideLoading() {
        this.view.hideLoading();
    }

    private void showToast(int stringResource) {
        this.view.showToast(stringResource);
    }

    @Override
    public void destroy() {
        this.view = null;
        this.recyclerItemPosition.setOffset(0);
        this.recyclerItemPosition.setMovieId(0);

        if (!this.moviePageDisposable.isDisposed()) {
            this.moviePageDisposable.dispose();
        }

        if (!this.movieDisposable.isDisposed()) {
            this.movieDisposable.dispose();
        }

        if (!this.movieListPagesDisposable.isDisposed()) {
            this.movieListPagesDisposable.dispose();
        }
    }

    private class GetMoviesObserver extends DisposableObserver<MoviesEntity> {
        @Override
        public void onNext(MoviesEntity moviesEntity) {
            MovieListPresenter.this.moviesEntity = moviesEntity;
            MovieListPresenter.this.view.renderMoviesList(moviesEntity);
            MovieListPresenter.this.isActionAllowed = true;
            MovieListPresenter.this.hideLoading();
            MovieListPresenter.this.scrollToMovieIfPossible(moviesEntity);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieListPresenter.this.view.showToast(R.string.main_error);
            MovieListPresenter.this.hideLoading();
            MovieListPresenter.this.isActionAllowed = true;
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "GetMoviesObserver onComplete");
        }
    }

    private class GetMovieListForPagesObserver extends DisposableObserver<MoviesEntity> {
        @Override
        public void onNext(MoviesEntity moviesEntity) {
            MovieListPresenter.this.moviesEntity = moviesEntity;
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieListPresenter.this.view.showToast(R.string.main_error);
            MovieListPresenter.this.hideLoading();
            MovieListPresenter.this.isActionAllowed = true;
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "GetMoviesObserver onComplete");
            MovieListPresenter.this.view.renderMoviesList(moviesEntity);
            MovieListPresenter.this.isActionAllowed = true;
            MovieListPresenter.this.hideLoading();
            MovieListPresenter.this.scrollToMovieIfPossible(moviesEntity);
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

/*    private class GetMovieListForAllPagesObserver extends DisposableObserver<MoviesEntity> {
        @Override
        public void onNext(MoviesEntity moviesEntity) {
            MovieListPresenter.this.moviesEntity = moviesEntity;
            MovieListPresenter.this.view.renderMoviesList(MovieListPresenter.this.moviesEntity);
            MovieListPresenter.this.hideLoading();
            MovieListPresenter.this.isActionAllowed = true;
            MovieListPresenter.this.scrollToMovieIfPossible(moviesEntity);
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
    }*/
}

