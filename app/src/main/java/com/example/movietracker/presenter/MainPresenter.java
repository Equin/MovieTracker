package com.example.movietracker.presenter;

import android.graphics.Movie;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieFilter;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.view.contract.MainView;
import com.example.movietracker.view.model.Option;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class MainPresenter extends BasePresenter {

    private ModelContract.GenreModel genreModel;
    private MainView mainView;
    private MovieFilter movieFilter;
    private GenresEntity genresEntity;

    public MainPresenter(MainView mainView, ModelContract.GenreModel genreModel, MovieFilter movieFilter) {
        this.mainView = mainView;
        this.genreModel = genreModel;
        this.movieFilter = movieFilter;
    }

    public void getGenres() {
        showLoading();

    /*    MovieRepository movieRepository;

        movieRepository.getGenres().firstOrError()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<GenresEntity>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(GenresEntity genresEntity) {

            }

            @Override
            public void onError(Throwable e) {

            }
        });*/

        this.genreModel.getGenres(new GetGenresObserver());
    }

    public void getLocalGenres() {
        this.mainView.renderGenreView(this.genresEntity);
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

    public void onSearchButtonClicked(Option option) {
        this.movieFilter.setPage(1);
        this.movieFilter.setIncludeAdult(false);
        this.movieFilter.setSortBy(option.getSortBy().getSearchName());
        this.movieFilter.setOrder(option.getSortOrder());
        this.movieFilter.setSelectedGenres(this.genresEntity);
        this.mainView.openMovieListView(this.genresEntity);
    }

    public void onCancelButtonClicked() {
       this.mainView.dismissAllSelections();
    }

    public void onFilterButtonClicked() {
        this.mainView.openAlertDialog();
    }

    public void onGenreChecked(String text, boolean isChecked) {
        for(int i = 0; i < this.genresEntity.getGenres().size(); i++) {
            if (this.genresEntity.getGenres().get(i).getGenreName()
                    .equals(text)) {
                this.genresEntity.getGenres().get(i).setSelected(isChecked);
            }
        }
    }

    private class GetGenresObserver extends DefaultObserver<GenresEntity> {
        @Override
        public void onNext(GenresEntity genreList) {
            genresEntity = genreList;
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

