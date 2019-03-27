package com.example.movietracker.view.contract;

import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;

public interface MovieDetailsView extends View {
    void renderMovieDetails(MovieDetailsEntity movieDetailsEntity);
}
