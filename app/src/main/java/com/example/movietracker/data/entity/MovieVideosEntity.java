package com.example.movietracker.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideosEntity {

    @SerializedName("id")
    private int movieId;

    @SerializedName("results")
    private List<MovieVideoResultEntity> movieVideoResultEntities;

    public MovieVideosEntity(int movieId, List<MovieVideoResultEntity> movieVideoResultEntities) {
        this.movieId = movieId;
        this.movieVideoResultEntities = movieVideoResultEntities;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public List<MovieVideoResultEntity> getMovieVideoResultEntities() {
        return movieVideoResultEntities;
    }

    public void setMovieVideoResultEntities(List<MovieVideoResultEntity> movieVideoResultEntities) {
        this.movieVideoResultEntities = movieVideoResultEntities;
    }
}
