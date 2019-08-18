package com.example.movietracker.data.database.dao;

import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.MovieWithGenres;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastResultEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewResultEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideoResultEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;

@Dao
public interface MovieDetailDao {

    @Query("SELECT * FROM MovieCastResultEntity WHERE movieId = :movieId")
    Observable<List<MovieCastResultEntity>> getCasts(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCasts(List<MovieCastResultEntity> movieCastResultEntities);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM MovieDetailsEntity WHERE movieId = :movieId")
    Observable<List<MovieDetailsEntity>> getMovieInfo(int movieId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM GenreEntity " +
            "INNER JOIN MovieWithGenres " +
            "ON GenreEntity.genreId = MovieWithGenres.genre_id " +
            "WHERE MovieWithGenres.movie_id = :movieId")
    Observable<List<GenreEntity>> getGenres(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveInfo(MovieDetailsEntity movieDetailsEntity);

    @Query("SELECT * FROM MovieVideoResultEntity WHERE movieId = :movieId")
    Observable<List<MovieVideoResultEntity>> getMovieVideos(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMovieVideos(List<MovieVideoResultEntity> movieVideoResultEntityList);

    @Query("SELECT * FROM MovieReviewResultEntity WHERE movieId = :movieId")
    Observable<List<MovieReviewResultEntity>> getMovieReviews(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMovieReviews(List<MovieReviewResultEntity> movieReviewResultEntities);
}
