package com.example.movietracker.data.database.dao;

import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastResultEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewResultEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideoResultEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Observable;

@Dao
public interface MovieDetailDao {

    @Query("SELECT * FROM MovieCastResultEntity WHERE movieId = :movieId")
    Observable<List<MovieCastResultEntity>> getCasts(int movieId);

    @Insert
    void saveCasts(List<MovieCastResultEntity> movieCastResultEntities);

    @Query("SELECT * FROM MovieDetailsEntity WHERE movieId = :movieId")
    Observable<MovieDetailsEntity> getMovieInfo(int movieId);

    @Insert
    void saveInfo(MovieDetailsEntity movieDetailsEntity);

    @Query("SELECT * FROM MovieVideoResultEntity WHERE movieId = :movieId")
    Observable<List<MovieVideoResultEntity>> getMovieVideos(int movieId);

    @Insert
    void saveMovieVideos(List<MovieVideoResultEntity> movieVideoResultEntityList);

    @Query("SELECT * FROM MovieReviewResultEntity WHERE movieId = :movieId")
    Observable<List<MovieReviewResultEntity>> getMovieReviews(int movieId);

    @Insert
    void saveMovieReviews(List<MovieReviewResultEntity> movieReviewResultEntities);
}
