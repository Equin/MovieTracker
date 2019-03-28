package com.example.movietracker.data.entity;

import com.google.gson.annotations.SerializedName;

public class MovieRequestEntity {

    private int page;
    private String genresId;
    private boolean includeAdult;
    private String sortBy;

    public MovieRequestEntity() {

    }

    public MovieRequestEntity(int page, String genresId, boolean includeAdult, String sortBy) {
        this.page = page;
        this.genresId = genresId;
        this.includeAdult = includeAdult;
        this.sortBy = sortBy;
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

    public boolean isIncludeAdult() {
        return includeAdult;
    }

    public void setIncludeAdult(boolean includeAdult) {
        this.includeAdult = includeAdult;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
