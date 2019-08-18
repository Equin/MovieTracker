package com.example.movietracker.view.contract;

import com.example.movietracker.data.entity.movie.MoviesEntity;
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
    void openLoginDialog();

    void dismissAllSelections();
    void dismissDialog();

    void setParentalControlEnabled(boolean parentalControlEnabled);

    void stopBackgroundSync();
    void startBackgroundSync();
    void setBackgroundSyncEnabled(boolean backgroundSyncEnabled);

    void showLoginMenuItem();
    void hideLoginMenuItem();

    void showLogoutMenuItem();
    void hideLogoutMenuItem();

    void setUsernameToHeaderView(String tmdbUsername);
}
