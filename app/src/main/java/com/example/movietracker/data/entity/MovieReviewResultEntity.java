package com.example.movietracker.data.entity;

import com.google.gson.annotations.SerializedName;

public class MovieReviewResultEntity {

    @SerializedName("id")
    private String reviewId;

    @SerializedName("author")
    private String reviewAuthor;

    @SerializedName("content")
    private String reviewContent;

    @SerializedName("url")
    private String reviewUrl;

    public MovieReviewResultEntity(String reviewId, String reviewAuthor, String reviewContent, String reviewUrl) {
        this.reviewId = reviewId;
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
        this.reviewUrl = reviewUrl;
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
}
