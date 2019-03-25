package com.example.movietracker.data.entity;

import com.google.gson.annotations.SerializedName;

public class MovieDetailsEntity {

    @SerializedName("id")
    private int movieId;

    @SerializedName("genres")
    private GenresEntity genresEntity;

    @SerializedName("imdb_id")
    private int imdbId;

    @SerializedName("title")
    private String movieTitle;

    @SerializedName("tagline")
    private String movieTagline;

    @SerializedName("vote_count")
    private int imdbVoteCount;

    @SerializedName("vote_average")
    private int imdbVoteAverage;







}
