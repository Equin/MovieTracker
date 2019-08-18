package com.example.movietracker.data.entity.genre;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GenresEntity implements Serializable {

    @SerializedName("genres")
    private List<GenreEntity> genres;

    public GenresEntity() {

    }

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
