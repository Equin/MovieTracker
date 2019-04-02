package com.example.movietracker.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

public class MoviesEntity {

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<MovieResultEntity> movies;

    public MoviesEntity() {

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
}
