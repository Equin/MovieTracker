package com.example.movietracker.data.net.api;

import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.data.entity.movie.MoviesEntity;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("genre/movie/list")
    Single<GenresEntity> getGenres();

    @GET("discover/movie/")
    Observable<MoviesEntity> getMovies(@Query("with_genres") String genresIds, @Query("sort_by") String sortBy, @Query("page") int page, @Query("include_adult") boolean isIncludeAdult);

   /* @GET("discover/movie/")
    Observable<MoviesEntity> getMoviesForPages(@Query("with_genres") String genresIds, @Query("sort_by") String sortBy, @Query("page") int page, @Query("include_adult") boolean isIncludeAdult);*/

    @GET("movie/{movie_id}")
    Observable<MovieDetailsEntity> getMovieDetailsById(@Path("movie_id") int movieId);

    @GET("movie/{movie_id}/videos")
    Observable<MovieVideosEntity> getMovieVideos(@Path("movie_id") int movieId);

    @GET("movie/{movie_id}/credits")
    Observable<MovieCastsEntity> getMovieCasts(@Path("movie_id") int movieId);

    @GET("movie/{movie_id}/reviews")
    Observable<MovieReviewsEntity> getMovieReviews(@Path("movie_id") int movieId);

    @GET("search/movie/")
    Observable<MoviesEntity> getMoviesByTitle(@Query("query") String query, @Query("include_adult")boolean isIncludeAdult);

}
