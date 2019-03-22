package com.example.movietracker.view.contract;

import com.example.movietracker.data.entity.GenreEntity;
import com.example.movietracker.data.entity.GenresEntity;

import java.util.List;

public interface MainView extends View {
    void renderGenreView(GenresEntity genreList);
    void openMovieListView(GenresEntity genreList);

}
