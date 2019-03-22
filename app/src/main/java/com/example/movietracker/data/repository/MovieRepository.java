package com.example.movietracker.data.repository;

import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.data.entity.MovieListEntity;

import java.util.List;

import io.reactivex.Observable;

public interface MovieRepository {
    Observable<GenresEntity> getGenres();
    Observable<MovieListEntity> getMovies(String genresId);
}
