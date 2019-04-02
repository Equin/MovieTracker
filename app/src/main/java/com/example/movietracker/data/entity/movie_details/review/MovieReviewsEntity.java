package com.example.movietracker.data.entity.movie_details.review;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviewsEntity {

    @SerializedName("id")
    private int movieId;

    @SerializedName("results")
    private List<MovieReviewResultEntity> reviews;

    public MovieReviewsEntity(int movieId, List<MovieReviewResultEntity> reviews) {
        this.movieId = movieId;
        this.reviews = reviews;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public List<MovieReviewResultEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<MovieReviewResultEntity> reviews) {
        this.reviews = reviews;
    }
}
