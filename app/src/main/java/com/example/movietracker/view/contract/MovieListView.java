package com.example.movietracker.view.contract;

import com.example.movietracker.data.entity.MoviesEntity;

public interface MovieListView extends DownloadListenerView {
    void renderMoviesList(MoviesEntity moviesEntity);
    void renderAdditionalMovieListPage(MoviesEntity moviesEntity);
    void showMovieDetailScreen(int movieId);
    void scrollToPositionWithOffset(int itemPosition, int itemOffset);
    void displayNothingToShowHint();
}
