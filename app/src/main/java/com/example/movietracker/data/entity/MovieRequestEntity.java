package com.example.movietracker.data.entity;

import com.google.gson.annotations.SerializedName;

public class MovieRequestEntity {

    private int page;
    private String genresId;

    public MovieRequestEntity() {

    }

    public MovieRequestEntity(int page, String genresId) {
        this.page = page;
        this.genresId = genresId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getGenresId() {
        return genresId;
    }

    public void setGenresId(String genresId) {
        this.genresId = genresId;
    }
}
