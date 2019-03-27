package com.example.movietracker.data.net.api;

import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.data.entity.MoviesEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("genre/movie/list")
    Observable<GenresEntity> getGenres();

    @GET("discover/movie/")
    Observable<MoviesEntity> getMoviesByGenres(@Query("with_genres") String genresIds);

    @GET("movie/{movie_id}")
    Observable<MovieDetailsEntity> getMovieDetailsById(@Path("movie_id") int movieId);

    @GET("movie/{movie_id}/videos")
    Observable<MovieVideosEntity> getMovieVideos(@Path("movie_id") int movieId);

    @GET("movie/{movie_id}/credits")
    Observable<MovieCastsEntity> getMovieCasts(@Path("movie_id") int movieId);

    @GET("movie/{movie_id}/reviews")
    Observable<MovieReviewsEntity> getMovieReviews(@Path("movie_id") int movieId);

}
