package com.example.movietracker.data.repository;

import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.data.entity.MovieCastsEntity;
import com.example.movietracker.data.entity.MovieDetailsEntity;
import com.example.movietracker.data.entity.MovieReviewsEntity;
import com.example.movietracker.data.entity.MovieVideosEntity;
import com.example.movietracker.data.entity.MoviesEntity;
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
    public Observable<MoviesEntity> getMovies(String genresIds) {
        return this.movieApi.getMoviesByGenres(genresIds);
    }

    @Override
    public Observable<MovieDetailsEntity> getMovieDetails(int movieId) {
        return this.movieApi.getMovieDetailsById(movieId);
    }

    @Override
    public Observable<MovieCastsEntity> getMovieCasts(int movieId) {
        return this.movieApi.getMovieCasts(movieId);
    }

    @Override
    public Observable<MovieVideosEntity> getMovieVideos(int movieId) {
        return this.movieApi.getMovieVideos(movieId);
    }

    @Override
    public Observable<MovieReviewsEntity> getMovieReviews(int movieId) {
        return this.movieApi.getMovieReviews(movieId);
    }
}
