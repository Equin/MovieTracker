package com.example.movietracker.data.entity.movie_details.review;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class MovieReviewResultEntity {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    private String reviewId;

    @SerializedName("author")
    private String reviewAuthor;

    @SerializedName("content")
    private String reviewContent;

    @SerializedName("url")
    private String reviewUrl;

    private int movieId;

    @Ignore
    public MovieReviewResultEntity(@NonNull String reviewId, String reviewAuthor, String reviewContent, String reviewUrl) {
        this.reviewId = reviewId;
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
        this.reviewUrl = reviewUrl;
    }

    public MovieReviewResultEntity(@NonNull String reviewId, String reviewAuthor, String reviewContent, String reviewUrl, int movieId) {
        this.reviewId = reviewId;
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
        this.reviewUrl = reviewUrl;
        this.movieId = movieId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
