package com.example.movietracker.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class MovieEntity {

    @SerializedName("id")
    private int movieId;

    @SerializedName("title")
    private String movieTitle;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("genre_ids")
    private List<Integer> genreIds;

    @SerializedName("adult")
    private boolean isAdult;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private Date releaseDate;

    public MovieEntity(int movieId, String movieTitle, String posterPath, List<Integer> genreIds, boolean isAdult, String overview, Date releaseDate) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.posterPath = posterPath;
        this.genreIds = genreIds;
        this.isAdult = isAdult;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public int getMovieId() {
        return this.movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return this.movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getPosterPath() {
        return this.posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public List<Integer> getGenreIds() {
        return this.genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public boolean isAdult() {
        return this.isAdult;
    }

    public void setAdult(boolean adult) {
        this.isAdult = adult;
    }

    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
