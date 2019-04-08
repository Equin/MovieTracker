package com.example.movietracker.di;

import com.example.movietracker.data.entity.MovieRequestEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;

public class DataProvider {

    public static GenresEntity genresEntity;
    public static MovieRequestEntity movieRequestEntity;

    public static void initialize() {
        genresEntity = new GenresEntity();
        movieRequestEntity = new MovieRequestEntity();
    }

    public static void onDestroy() {
        genresEntity = null;
        movieRequestEntity = null;
    }
}
