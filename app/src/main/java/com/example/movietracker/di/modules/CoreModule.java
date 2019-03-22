package com.example.movietracker.di.modules;

import android.content.Context;

import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.di.PerActivity;
import com.example.movietracker.interactor.genre.GetGenresUseCase;
import com.example.movietracker.interactor.genre.GetMoviesUseCase;
import com.example.movietracker.presenter.MainPresenter;
import com.example.movietracker.view.custom_view.GenreView;

import dagger.Module;
import dagger.Provides;

@Module
public class CoreModule {

    @Provides
    @PerActivity
    GetGenresUseCase provideGetGenresUseCase(
            MovieRepository movieRepository) {
        return new GetGenresUseCase(
                movieRepository);
    }

    @Provides
    @PerActivity
    GetMoviesUseCase provideGetMoviesUseCase(
            MovieRepository movieRepository) {
        return new GetMoviesUseCase(
                movieRepository);
    }

    @Provides
    @PerActivity
    MainPresenter provideMainPresenter(GetGenresUseCase getGenresUseCase) {
        return new MainPresenter(getGenresUseCase);
    }

    @Provides
    @PerActivity
    GenreView provideGenreView(Context context) {
        return new GenreView(context);
    }
}