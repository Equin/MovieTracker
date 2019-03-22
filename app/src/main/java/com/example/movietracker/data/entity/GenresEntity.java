package com.example.movietracker.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenresEntity {

    @SerializedName("genres")
    private List<GenreEntity> genres;

    public GenresEntity(List<GenreEntity> genres) {
        this.genres = genres;
    }

    public List<GenreEntity> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreEntity> genres) {
        this.genres = genres;
    }
}
