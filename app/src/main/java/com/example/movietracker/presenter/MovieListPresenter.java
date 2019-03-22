package com.example.movietracker.presenter;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.GenreEntity;
import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.data.entity.MovieListEntity;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.interactor.genre.GetGenresUseCase;
import com.example.movietracker.interactor.genre.GetMoviesUseCase;
import com.example.movietracker.view.contract.MainView;
import com.example.movietracker.view.contract.MovieListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

public class MovieListPresenter extends BasePresenter {

    private GetMoviesUseCase getMoviesUseCase;

    private MovieListView view;

    public MovieListPresenter(GetMoviesUseCase getMoviesUseCase) {
            this.getMoviesUseCase = getMoviesUseCase;
    }

    public void setView(MovieListView view) {
        this.view = view;
    }

    public void initialize(GenresEntity genresEntity) {
        this.view.showToast(R.string.app_name);
        this.getGenres(genresEntity);
    }

    private void getGenres(GenresEntity genresEntity) {
        List<GenreEntity> genreEntity = genresEntity.getGenres();
        StringBuilder sb = new StringBuilder();

        for(GenreEntity genre : genreEntity) {
            sb.append(genre.getGenreId()).append(",");
        }

        this.getMoviesUseCase.execute(new GetMoviesObserver(), sb.toString());
    }

    @Override
    public void destroy() {
        this.view = null;
    }


    private class GetMoviesObserver extends DefaultObserver<MovieListEntity> {
        @Override
        public void onNext(MovieListEntity movieListEntity) {
            MovieListPresenter.this.view.renderMoviesList(movieListEntity);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieListPresenter.this.view.showToast(R.string.main_error);
        }
    }

}

