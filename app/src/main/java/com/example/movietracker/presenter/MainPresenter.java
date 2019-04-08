package com.example.movietracker.presenter;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.di.DataProvider;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.model.model_impl.GenreModelImpl;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.view.contract.MainView;
import com.example.movietracker.view.model.Option;

import io.reactivex.annotations.NonNull;

public class MainPresenter extends BasePresenter {

    private ModelContract.GenreModel genreModel;
    private MainView mainView;

    public MainPresenter(MainView mainView) {
        this.mainView = mainView;
        this.genreModel = new GenreModelImpl();
    }

    public void getGenres() {
        showLoading();
        this.genreModel.getGenres(new GetGenresObserver());
    }

    private void showLoading() {
        this.mainView.showLoading();
    }

    private void hideLoading() {
        this.mainView.hideLoading();
    }

    @Override
    public void destroy() {
        this.mainView = null;
        this.genreModel.stop();
    }

    public void onSearchButtonClicked(GenresEntity genresEntity, Option option) {
        DataProvider.movieRequestEntity.setPage(1);
        DataProvider.movieRequestEntity.setIncludeAdult(false);
        DataProvider.movieRequestEntity.setSortBy(option.getSortBy().getSearchName());
        DataProvider.movieRequestEntity.setOrder(option.getSortOrder());
        DataProvider.movieRequestEntity.setGenresEntity(genresEntity);

       this.mainView.openMovieListView(DataProvider.movieRequestEntity);
    }

    public void onCancelButtonClicked() {
       this.mainView.dismissAllSelections();
    }

    public void onFilterButtonClicked() {
        this.mainView.openAlertDialog();
    }

    private class GetGenresObserver extends DefaultObserver<GenresEntity> {
        @Override
        public void onNext(GenresEntity genreList) {
            MainPresenter.this.mainView.renderGenreView(genreList);
            MainPresenter.this.hideLoading();
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MainPresenter.this.mainView.showToast(R.string.main_error);
            MainPresenter.this.hideLoading();
        }
    }
}

