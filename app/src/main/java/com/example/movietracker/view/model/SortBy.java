package com.example.movietracker.view.model;

public enum SortBy {

    POPULARITY("Popularity", "popularity"),
    RATING("Rating", "vote_average"),
    RELEASE_DATE("Release Date", "release_date"),
    TITLE("Title", "original_title");

    private String displayName;
    private String searchName;

    SortBy(String displayName, String searchName ) {
        this.displayName = displayName;
        this.searchName = searchName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getSearchName() {
        return this.searchName;
    }
}