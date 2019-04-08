package com.example.movietracker.presenter;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieRequestEntity;
import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.model.model_impl.MovieModelImpl;
import com.example.movietracker.view.contract.MovieListView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class MovieListPresenter extends BasePresenter {

    private MovieListView view;
    private ModelContract.MovieModel movieModel;
    private MoviesEntity moviesEntity;

    public MovieListPresenter(MovieListView view) {
        this.view = view;
        this.movieModel = new MovieModelImpl();
        this.moviesEntity = new MoviesEntity();
        initialize();
    }

    private void initialize() {
        showLoading();
    }

    public void onMovieItemClicked(int clickedMovieId) {
        this.view.showMovieDetailScreen(clickedMovieId);
    }

    public void getMoviesByFilters(MovieRequestEntity movieRequestEntity) {
        showLoading();
        getMovies(movieRequestEntity);
    }

    public void getMoviesWithPagination(MovieRequestEntity movieRequestEntity) {
        showLoading();
        getMoviesByPage(movieRequestEntity);
    }

    public MoviesEntity getMoviesEntity() {
        return this.moviesEntity;
    }

    private void getMovies(MovieRequestEntity movieRequestEntity) {
        this.movieModel.getMovies(new GetMoviesObserver(), movieRequestEntity);
    }

    private void getMoviesByPage(MovieRequestEntity movieRequestEntity) {
        this.movieModel.getMovies(new GetMoviesPageObserver(), movieRequestEntity);
    }

    private void showLoading() {
        this.view.showLoading();
    }

    private void hideLoading() {
        this.view.hideLoading();
    }

    @Override
    public void destroy() {
        this.movieModel.stop();
        this.view = null;
    }


    private class GetMoviesObserver extends DefaultObserver<MoviesEntity> {
        @Override
        public void onNext(MoviesEntity moviesEntity) {
            MovieListPresenter.this.moviesEntity = moviesEntity;
            MovieListPresenter.this.view.renderMoviesList(moviesEntity);
            MovieListPresenter.this.hideLoading();
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieListPresenter.this.view.showToast(R.string.main_error);
            MovieListPresenter.this.hideLoading();
        }
    }

    private class GetMoviesPageObserver extends DefaultObserver<MoviesEntity> {
        @Override
        public void onNext(MoviesEntity moviesEntity) {
            MovieListPresenter.this.moviesEntity.setPage(moviesEntity.getPage());
            MovieListPresenter.this.moviesEntity.setTotalPages(moviesEntity.getTotalPages());
            MovieListPresenter.this.moviesEntity.addMovies(moviesEntity.getMovies());
            MovieListPresenter.this.view.renderAdditionalMovieListPage(MovieListPresenter.this.moviesEntity);
            MovieListPresenter.this.hideLoading();
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieListPresenter.this.view.showToast(R.string.main_error);
            MovieListPresenter.this.hideLoading();
        }
    }
}

