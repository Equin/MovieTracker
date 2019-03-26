package com.example.movietracker.view.contract;

import com.example.movietracker.data.entity.MovieDetailsEntity;

public interface MovieDetailsView extends View {
    void renderMovieDetails(MovieDetailsEntity movieDetailsEntity);
}
