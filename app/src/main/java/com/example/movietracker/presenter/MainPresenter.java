package com.example.movietracker.presenter;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.GenreEntity;
import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.interactor.genre.GetGenresUseCase;
import com.example.movietracker.view.contract.MainView;
import com.example.movietracker.view.contract.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

public class MainPresenter extends BasePresenter {

    private  GetGenresUseCase getGenresUseCase;

    private MainView mainView;

    @Inject
    public MainPresenter(GetGenresUseCase getGenresUseCase) {
            this.getGenresUseCase = getGenresUseCase;
    }

    public void setView(MainView mainView) {
        this.mainView = mainView;
    }

    public void initialize() {
        this.mainView.showToast(R.string.app_name);
        this.getGenres();
    }

    private void getGenres() {
        this.getGenresUseCase.execute(new GetGenresObserver(), null);
    }

    @Override
    public void destroy() {
        this.mainView = null;
        this.getGenresUseCase.dispose();
    }

    public void onSearchButtonClicked() {
       this.mainView.openMovieListView();
    }

    private class GetGenresObserver extends DefaultObserver<GenresEntity> {
        @Override
        public void onNext(GenresEntity genreList) {
            MainPresenter.this.mainView.renderGenreView(genreList);
        }


        @Override
        public void onError(@NonNull Throwable e) {
            MainPresenter.this.mainView.showToast(R.string.main_error);
        }
    }

}

