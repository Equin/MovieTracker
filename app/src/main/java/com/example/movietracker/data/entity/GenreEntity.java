package com.example.movietracker.data.entity;

import com.google.gson.annotations.SerializedName;

public class GenreEntity {

    @SerializedName("id")
    private int genreId;

    @SerializedName("name")
    private String genreName;

    public GenreEntity(int genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }

    public int getGenreId() {
        return this.genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return this.genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
