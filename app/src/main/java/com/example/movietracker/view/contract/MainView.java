package com.example.movietracker.view.contract;

import com.example.movietracker.data.entity.MovieRequestEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;

public interface MainView extends View {
    void renderGenreView(GenresEntity genreList);
    void openMovieListView(MovieRequestEntity movieRequestEntity);
    void openAlertDialog();
    void dismissAllSelections();

}
