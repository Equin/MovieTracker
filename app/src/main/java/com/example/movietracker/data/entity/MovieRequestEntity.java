package com.example.movietracker.data.entity;

import java.util.List;

public class MovieRequestEntity {

    private int page;
    private List<Integer> genresId;
    private boolean includeAdult;
    private String sortBy;

    public MovieRequestEntity() {

    }

    public MovieRequestEntity(int page, List<Integer> genresId, boolean includeAdult, String sortBy) {
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

    public List<Integer> getGenresId() {
        return genresId;
    }

    public void setGenresId(List<Integer> genresId) {
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
