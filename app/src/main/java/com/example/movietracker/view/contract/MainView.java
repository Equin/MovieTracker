package com.example.movietracker.view.contract;

import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;

public interface MainView extends View {
    void renderGenreView(GenresEntity genreList);

    void openMovieListView(GenresEntity genreList);
    void openMovieDetailScreen(int movieId);
    void openFavoriteMoviesListView(GenresEntity genreList);
    void openAlertDialog();
    void openNewPasswordDialog();
    void openCheckPasswordDialog();
    void openResetPasswordDialog();

    void dismissAllSelections();
    void dismissPasswordDialog();

    void setParentalControlEnabled(boolean parentalControlEnabled);

    void stopBackgroundSync();
    void startBackgroundSync();
    void setBackgroundSyncEnabled(boolean backgroundSyncEnabled);

    void showSearchResult(MoviesEntity moviesEntity);
}
