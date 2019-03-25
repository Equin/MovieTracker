package com.example.movietracker.view.contract;

import com.example.movietracker.data.entity.MoviesEntity;

public interface MovieListView extends View {
    void renderMoviesList(MoviesEntity moviesEntity);
    void showMovieDetailScreen(int movieId);
}
