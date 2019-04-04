package com.example.movietracker.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MovieResultEntity {

    @PrimaryKey
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
    private String movieOverview;

    @SerializedName("vote_average")
    private double movieVoteAverage;

    @SerializedName("release_date")
    private Date movieReleaseDate;

    @SerializedName("popularity")
    private double moviePopularity;

    public MovieResultEntity(int movieId, String movieTitle, String posterPath, List<Integer> genreIds, boolean isAdult, String movieOverview, double movieVoteAverage, Date movieReleaseDate, double moviePopularity) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.posterPath = posterPath;
        this.genreIds = genreIds;
        this.isAdult = isAdult;
        this.movieOverview = movieOverview;
        this.movieVoteAverage = movieVoteAverage;
        this.movieReleaseDate = movieReleaseDate;
        this.moviePopularity = moviePopularity;
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

    public String getMovieOverview() {
        return this.movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public double getMovieVoteAverage() {
        return this.movieVoteAverage;
    }

    public void setMovieVoteAverage(double movieVoteAverage) {
        this.movieVoteAverage = movieVoteAverage;
    }

    public Date getMovieReleaseDate() {
        return this.movieReleaseDate;
    }

    public void setMovieReleaseDate(Date movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public double getMoviePopularity() {
        return moviePopularity;
    }

    public void setMoviePopularity(double moviePopularity) {
        this.moviePopularity = moviePopularity;
    }
}
