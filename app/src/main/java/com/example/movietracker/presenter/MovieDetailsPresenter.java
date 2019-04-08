package com.example.movietracker.presenter;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.model.model_impl.MovieInfoModelImpl;
import com.example.movietracker.view.contract.MovieDetailsView;

import io.reactivex.annotations.NonNull;

public class MovieDetailsPresenter extends BasePresenter {

    private final ModelContract.MovieInfoModel movieInfoModel;

    private MovieDetailsView view;

    public MovieDetailsPresenter(MovieDetailsView movieDetailsView) {
        this.movieInfoModel = new MovieInfoModelImpl();
        this.view = movieDetailsView;
    }

    public void getMovieDetails(int movieId) {
        showLoading();
        this.movieInfoModel.getMovieInfo(new GetMovieDetailsObserver(), movieId);
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
        this.movieInfoModel.stop();
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

