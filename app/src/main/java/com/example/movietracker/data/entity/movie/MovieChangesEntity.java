package com.example.movietracker.data.entity.movie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieChangesEntity {

    @SerializedName("results")
    private List<MovieChangesResultEntity> results;

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;

    public MovieChangesEntity(List<MovieChangesResultEntity> results, int page, int totalPages, int totalResults) {
        this.results = results;
        this.page = page;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public MovieChangesEntity() {
    }

    public void addMovieChanges(List<MovieChangesResultEntity> result) {
        this.results.addAll(result);
    }

    public List<MovieChangesResultEntity> getResults() {
        return results;
    }

    public void setResults(List<MovieChangesResultEntity> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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
}
