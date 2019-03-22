package com.example.movietracker.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieListEntity {

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<MovieEntity> movies;

    public MovieListEntity(int page, int totalPages, List<MovieEntity> movies) {
        this.page = page;
        this.totalPages = totalPages;
        this.movies = movies;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<MovieEntity> getMovies() {
        return this.movies;
    }

    public void setMovies(List<MovieEntity> movies) {
        this.movies = movies;
    }
}
