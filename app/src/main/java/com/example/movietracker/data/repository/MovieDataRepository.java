package com.example.movietracker.data.repository;

import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.data.entity.MovieListEntity;
import com.example.movietracker.data.net.RestClient;
import com.example.movietracker.data.net.api.MovieApi;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class MovieDataRepository implements MovieRepository {

    private final MovieApi movieApi;

    @Inject
    public MovieDataRepository(RestClient restClient) {
        this.movieApi = restClient.getMovieApi();
    }

    @Override
    public Observable<GenresEntity> getGenres() {
        return this.movieApi.getGenres();
    }

    @Override
    public Observable<MovieListEntity> getMovies(String genresIds) {
        return this.movieApi.getMoviesByGenres(genresIds);
    }
}
