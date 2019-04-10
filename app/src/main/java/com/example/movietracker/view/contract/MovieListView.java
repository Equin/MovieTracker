package com.example.movietracker.view.contract;

import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.view.model.MovieRecyclerItemPosition;

public interface MovieListView extends View {
    void renderMoviesList(MoviesEntity moviesEntity);
    void renderAdditionalMovieListPage(MoviesEntity moviesEntity);
    void showMovieDetailScreen(int movieId);
    void scrollToMovie(int itemPosition, int itemOffset);
}
