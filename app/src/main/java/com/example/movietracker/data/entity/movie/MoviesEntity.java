package com.example.movietracker.data.entity.movie;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MoviesEntity {

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<MovieResultEntity> movies;

    public MoviesEntity() {
        movies = new ArrayList<>();
    }

    public MoviesEntity(int page, int totalPages, List<MovieResultEntity> movieResultEntities) {
        this.page = page;
        this.totalPages = totalPages;
        this.movies = movieResultEntities;
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

    public List<MovieResultEntity> getMovies() {
        return this.movies;
    }

    public void setMovies(List<MovieResultEntity> movies) {
        this.movies = movies;
    }

    public void addMovies(List<MovieResultEntity> movies) {
        this.movies.addAll(movies);
    }
}
