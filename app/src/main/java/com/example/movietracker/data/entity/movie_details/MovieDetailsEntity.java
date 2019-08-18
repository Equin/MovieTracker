package com.example.movietracker.data.entity.movie_details;

import com.example.movietracker.data.entity.genre.GenreEntity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class MovieDetailsEntity implements Serializable {

    @SerializedName("id")
    @PrimaryKey
    private int movieId;

    @SerializedName("genres")
    @Ignore
    private List<GenreEntity> genres;

    @SerializedName("imdb_id")
    private String imdbId;

    @SerializedName("title")
    private String movieTitle;

    @SerializedName("poster_path")
    private String moviePosterPath;

    @SerializedName("tagline")
    private String movieTagline;

    @SerializedName("vote_count")
    private int imdbVoteCount;

    @SerializedName("vote_average")
    private double imdbVoteAverage;

    @SerializedName("overview")
    private String movieOverview;

    @SerializedName("release_date")
    private Date movieReleaseDate;

    @SerializedName("runtime")
    private int movieRuntime;

    public MovieDetailsEntity() {

    }

    public MovieDetailsEntity(int movieId,
                              List<GenreEntity> genres,
                              String imdbId, String movieTitle,
                              String movieTagline,
                              int imdbVoteCount,
                              double imdbVoteAverage,
                              String movieOverview,
                              Date movieReleaseDate,
                              int movieRuntime, String moviePosterPath) {
        this.movieId = movieId;
        this.genres = genres;
        this.imdbId = imdbId;
        this.movieTitle = movieTitle;
        this.movieTagline = movieTagline;
        this.imdbVoteCount = imdbVoteCount;
        this.imdbVoteAverage = imdbVoteAverage;
        this.movieOverview = movieOverview;
        this.movieReleaseDate = movieReleaseDate;
        this.movieRuntime = movieRuntime;
        this.moviePosterPath = moviePosterPath;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public  List<GenreEntity> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreEntity> genres) {
        this.genres = genres;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieTagline() {
        return movieTagline;
    }

    public void setMovieTagline(String movieTagline) {
        this.movieTagline = movieTagline;
    }

    public int getImdbVoteCount() {
        return imdbVoteCount;
    }

    public void setImdbVoteCount(int imdbVoteCount) {
        this.imdbVoteCount = imdbVoteCount;
    }

    public double getImdbVoteAverage() {
        return imdbVoteAverage;
    }

    public void setImdbVoteAverage(double imdbVoteAverage) {
        this.imdbVoteAverage = imdbVoteAverage;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public Date getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(Date movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public int getMovieRuntime() {
        return movieRuntime;
    }

    public void setMovieRuntime(int movieRuntime) {
        this.movieRuntime = movieRuntime;
    }

    public String getMoviePosterPath() {
        return moviePosterPath;
    }

    public void setMoviePosterPath(String moviePosterPath) {
        this.moviePosterPath = moviePosterPath;
    }
}
