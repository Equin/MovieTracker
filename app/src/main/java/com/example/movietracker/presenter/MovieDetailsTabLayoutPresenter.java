package com.example.movietracker.presenter;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieCastsEntity;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.interactor.use_cases.GetMovieCastsUseCase;
import com.example.movietracker.view.contract.TabLayoutView;

import io.reactivex.annotations.NonNull;

public class MovieDetailsTabLayoutPresenter extends BasePresenter {
    private GetMovieCastsUseCase getMovieCastsUseCase;

    private TabLayoutView<MovieCastsEntity> view;

    public MovieDetailsTabLayoutPresenter() {
        this.getMovieCastsUseCase = new GetMovieCastsUseCase();
    }

    public void setView(TabLayoutView<MovieCastsEntity> view) {
        this.view = view;
    }

    public void initialize(int movieId) {
        showLoading();
        getMovieCasts(movieId);
    }

    private void getMovieCasts(int movieId) {
        this.getMovieCastsUseCase.execute(new GetMovieCastObserver(), movieId);
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
        this.getMovieCastsUseCase.dispose();
    }

    private class GetMovieCastObserver extends DefaultObserver<MovieCastsEntity> {
        @Override
        public void onNext(MovieCastsEntity movieCastsEntity) {
            MovieDetailsTabLayoutPresenter.this.hideLoading();
            MovieDetailsTabLayoutPresenter.this.view.renderInfoToTab(movieCastsEntity);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieDetailsTabLayoutPresenter.this.view.showToast(R.string.main_error);
            MovieDetailsTabLayoutPresenter.this.hideLoading();
        }
    }

}
