package com.example.movietracker.presenter;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.di.DataProvider;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.model.model_impl.GenreModelImpl;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.view.contract.MainView;

import java.util.ArrayList;
import java.util.List;

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

    public void onSearchButtonClicked(GenresEntity genresEntity) {
       this.mainView.openMovieListView(getSelectedGenres(genresEntity));
    }

    private GenresEntity getSelectedGenres(GenresEntity genresEntity) {
        GenresEntity genres = new GenresEntity();
        List<GenreEntity> genreList = new ArrayList<>();

        if (genresEntity != null) {
            for (GenreEntity genre : genresEntity.getGenres()) {
                if (genre.isSelected()) {
                    genreList.add(genre);
                }
            }
        }

        genres.setGenres(genreList);

        return genres;
    }

    private class GetGenresObserver extends DefaultObserver<GenresEntity> {
        @Override
        public void onNext(GenresEntity genreList) {
            MainPresenter.this.mainView.renderGenreView(genreList);
            DataProvider.genresEntity = genreList;
            MainPresenter.this.hideLoading();
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MainPresenter.this.mainView.showToast(R.string.main_error);
            MainPresenter.this.hideLoading();
        }
    }
}

