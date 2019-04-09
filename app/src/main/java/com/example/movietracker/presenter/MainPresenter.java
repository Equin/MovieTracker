package com.example.movietracker.presenter;

import android.graphics.Movie;
import android.util.Log;

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
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter {

    private static final String TAG = MainPresenter.class.getCanonicalName();

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

        this.genreModel.getGenres()
        .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<GenresEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "Subscribed this.genreModel.getGenres()");
            }

            @Override
            public void onSuccess(GenresEntity genreList) {
                genresEntity = genreList;
                MainPresenter.this.mainView.renderGenreView(genreList);
                MainPresenter.this.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getLocalizedMessage());
                MainPresenter.this.mainView.showToast(R.string.main_error);
                MainPresenter.this.hideLoading();
            }
        });
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
        this.movieFilter = null;
        this.genresEntity = null;
        this.genreModel = null;
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
}

