package com.example.movietracker.data.entity.genre;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GenreEntity implements Serializable {

    @SerializedName("id")
    private int genreId;

    @SerializedName("name")
    private String genreName;

    private boolean isSelected;


    public GenreEntity(int genreId, String genreName, boolean isSelected) {
        this.genreId = genreId;
        this.genreName = genreName;
        this.isSelected = isSelected;
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

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
}
