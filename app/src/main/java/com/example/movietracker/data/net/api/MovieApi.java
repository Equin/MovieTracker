package com.example.movietracker.data.net.api;

import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.data.entity.MovieListEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MovieApi {

    @GET("genre/movie/list")
    Observable<GenresEntity> getGenres();

    @GET("genre/movie/list")
    Observable<MovieListEntity> getMoviesByGenres();
}
