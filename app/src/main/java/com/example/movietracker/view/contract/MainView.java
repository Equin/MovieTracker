package com.example.movietracker.view.contract;

import com.example.movietracker.data.entity.genre.GenresEntity;

public interface MainView extends View {
    void renderGenreView(GenresEntity genreList);

    void openMovieListView(GenresEntity genreList);
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
}
