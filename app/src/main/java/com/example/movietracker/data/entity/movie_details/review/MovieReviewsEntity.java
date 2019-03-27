package com.example.movietracker.data.entity.movie_details.review;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviewsEntity {

    @SerializedName("id")
    private int reviewsId;

    @SerializedName("page")
    private int reviewsPage;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("results")
    private List<MovieReviewResultEntity> reviews;

    public MovieReviewsEntity(int reviewsId, int reviewsPage, int totalPages, int totalResults, List<MovieReviewResultEntity> reviews) {
        this.reviewsId = reviewsId;
        this.reviewsPage = reviewsPage;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
        this.reviews = reviews;
    }

    public int getReviewsId() {
        return reviewsId;
    }

    public void setReviewsId(int reviewsId) {
        this.reviewsId = reviewsId;
    }

    public int getReviewsPage() {
        return reviewsPage;
    }

    public void setReviewsPage(int reviewsPage) {
        this.reviewsPage = reviewsPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<MovieReviewResultEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<MovieReviewResultEntity> reviews) {
        this.reviews = reviews;
    }
}
