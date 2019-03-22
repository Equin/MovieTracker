package com.example.movietracker.data.repository;

import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.data.entity.MovieListEntity;
import com.example.movietracker.data.net.RestClient;
import com.example.movietracker.data.net.api.MovieApi;

import io.reactivex.Observable;

public class MovieDataRepository implements MovieRepository {

    private MovieApi movieApi;

    private MovieDataRepository() {}

    private static class SingletonHelper {
        private static final MovieDataRepository INSTANCE = new MovieDataRepository();
    }

    public static MovieDataRepository getInstance(){
        return SingletonHelper.INSTANCE;
    }

    public void init(RestClient restClient) {
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
