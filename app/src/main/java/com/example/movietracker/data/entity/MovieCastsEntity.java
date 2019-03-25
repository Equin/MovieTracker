package com.example.movietracker.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieCastsEntity implements TabEntities{

    @SerializedName("id")
    private int movieId;

    @SerializedName("cast")
    private List<MovieCastResultEntity> movieCasts;

    public MovieCastsEntity(int movieId, List<MovieCastResultEntity> movieCasts) {
        this.movieId = movieId;
        this.movieCasts = movieCasts;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public List<MovieCastResultEntity> getMovieCasts() {
        return movieCasts;
    }

    public void setMovieCasts(List<MovieCastResultEntity> movieCasts) {
        this.movieCasts = movieCasts;
    }
}
