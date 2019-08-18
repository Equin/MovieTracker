package com.example.movietracker.data.entity.movie_details.cast;

import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class MovieCastResultEntity {

    @SerializedName("cast_id")
    @PrimaryKey
    private int castId;

    @SerializedName("name")
    private String castName;

    @SerializedName("profile_path")
    private String castImagePath;

    @SerializedName("order")
    private int castOrder;

    private int movieId;

    @Ignore
    public MovieCastResultEntity() {
    }

    @Ignore
    public MovieCastResultEntity(int castId, String castName, String castImagePath, int castOrder) {
        this.castId = castId;
        this.castName = castName;
        this.castImagePath = castImagePath;
        this.castOrder = castOrder;
    }

    public MovieCastResultEntity(int castId, String castName, String castImagePath, int castOrder, int movieId) {
        this.castId = castId;
        this.castName = castName;
        this.castImagePath = castImagePath;
        this.castOrder = castOrder;
        this.movieId = movieId;
    }

    public int getCastId() {
        return castId;
    }

    public void setCastId(int castId) {
        this.castId = castId;
    }

    public String getCastName() {
        return castName;
    }

    public void setCastName(String castName) {
        this.castName = castName;
    }

    public String getCastImagePath() {
        return castImagePath;
    }

    public void setCastImagePath(String castImagePath) {
        this.castImagePath = castImagePath;
    }

    public int getCastOrder() {
        return castOrder;
    }

    public void setCastOrder(int castOrder) {
        this.castOrder = castOrder;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
