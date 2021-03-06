package com.example.movietracker.data.entity.genre;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GenreEntity implements Serializable {

    @PrimaryKey
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

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;

        if(!(o instanceof GenreEntity)) return false;
        GenreEntity genreEntity = (GenreEntity)o;

        return this.genreId == genreEntity.genreId;
    }
}
