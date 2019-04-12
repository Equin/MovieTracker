package com.example.movietracker.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(primaryKeys = {"movie_id", "user_id"},
        indices = {@Index("movie_id"), @Index("user_id")},
        foreignKeys = {
                @ForeignKey(entity = MovieResultEntity.class,
                        parentColumns ="movieId",
                        childColumns ="movie_id"),
                @ForeignKey(entity=UserEntity.class,
                        parentColumns="userId",
                        childColumns="user_id")
        })
public class UserWithFavoriteMovies {
    @ColumnInfo(name = "movie_id")
    private int movieId;

    @ColumnInfo(name = "user_id")
    private int userId;

    public UserWithFavoriteMovies(int movieId, int userId) {
        this.movieId = movieId;
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
