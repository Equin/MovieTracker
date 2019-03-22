package com.example.movietracker.view.contract;

import com.example.movietracker.data.entity.MovieListEntity;

public interface MovieListView extends View {
    void renderMoviesList(MovieListEntity movieListEntity);
}
