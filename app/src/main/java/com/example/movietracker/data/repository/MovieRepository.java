package com.example.movietracker.data.repository;

import com.example.movietracker.data.database.MoviesDatabase;
import com.example.movietracker.data.entity.UserEntity;
import com.example.movietracker.data.entity.UserWithFavoriteMovies;
import com.example.movietracker.view.model.Filters;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.net.RestClient;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface MovieRepository {
    Single<GenresEntity> getGenres();
    Single<GenresEntity> getLocalGenres();
    Observable<MoviesEntity> getMovies(Filters filters);
    Observable<MoviesEntity> getMoviesWithFavorites(Filters filters);
    Observable<MoviesEntity> getMovieListForPages(Filters filters);
    Observable<MoviesEntity> getMovieListForPagesWithFavorites(Filters filters);
    Observable<MovieDetailsEntity> getMovieDetails(int movieId);
    Observable<MovieCastsEntity> getMovieCasts(int movieId);
    Observable<MovieVideosEntity> getMovieVideos(int movieId);
    Observable<MovieReviewsEntity> getMovieReviews(int movieId);
    Observable<UserEntity> getUser();
    Observable<UserEntity> getUserWithFavorites();
    void addUser(UserEntity userEntity);
    Completable updateUser(UserEntity userEntity);
    Completable deleteMovieFromFavorites(UserWithFavoriteMovies userWithFavoriteMovies);
    void init(RestClient restClient, MoviesDatabase moviesDatabase);
}
