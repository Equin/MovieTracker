package com.example.movietracker.di;

import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.data.entity.MoviesEntity;

public class DataProvider {

    public static GenresEntity genresEntity;
    public static MoviesEntity moviesEntity;

    public static void initialize() {
        genresEntity = new GenresEntity();
        moviesEntity = new MoviesEntity();
    }

}
