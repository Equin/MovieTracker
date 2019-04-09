package com.example.movietracker.presenter;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieFilter;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.model.model_impl.MovieModelImpl;
import com.example.movietracker.view.contract.MovieListView;

import io.reactivex.annotations.NonNull;

public class MovieListPresenter extends BasePresenter {

    private MovieListView view;
    private ModelContract.MovieModel movieModel;
    private MoviesEntity moviesEntity;

    public MovieListPresenter(MovieListView view) {
        this.view = view;
        this.movieModel = new MovieModelImpl();
        this.moviesEntity = new MoviesEntity();
    }

    public void onMovieItemClicked(int itemPosition) {
        this.view.showMovieDetailScreen(
                this.moviesEntity.getMovies().get(itemPosition).getMovieId());
    }

    public void getMoviesByFilters(MovieFilter movieFilter) {
        getMovies(movieFilter);
    }

    public void getLocalMovies() {
        this.view.renderMoviesList(this.moviesEntity);
    }

    public void getMoviesWithPagination(MovieFilter movieFilter) {
        getMoviesByPage(movieFilter);
    }

    public MoviesEntity getMoviesEntity() {
        return this.moviesEntity;
    }

    private void getMovies(MovieFilter movieFilter) {
        showLoading();
        this.movieModel.getMovies(new GetMoviesObserver(), movieFilter);
    }

    private void getMoviesByPage(MovieFilter movieFilter) {
        showLoading();
        this.movieModel.getMovies(new GetMoviesPageObserver(), movieFilter);
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

    private boolean isActionAllowed;

    public void lastMovieOfPageReached() {

        if(this.isActionAllowed) {
            this.isActionAllowed = false;
            if (this.moviesEntity.getTotalPages() > MovieFilter.getInstance().getPage()) {
                MovieFilter.getInstance().incrementPage();
                //showToast(MovieFilter.getInstance().getPage() + "page");
                this.getMoviesWithPagination(MovieFilter.getInstance());
            } else {
              //  showToast(R.string.movie_list_there_are_no_pages);
                this.isActionAllowed = true;
            }
        }
    }


    private class GetMoviesObserver extends DefaultObserver<MoviesEntity> {
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
    }

    private class GetMoviesPageObserver extends DefaultObserver<MoviesEntity> {
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
        }
    }
}

