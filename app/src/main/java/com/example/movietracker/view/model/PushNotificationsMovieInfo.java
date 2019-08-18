package com.example.movietracker.view.model;

import java.util.ArrayList;
import java.util.List;

public class PushNotificationsMovieInfo {

    private String title;
    private List<String> movieTitlesList;

    public PushNotificationsMovieInfo(String title, List<String> movieTitlesList) {
        this.title = title;
        this.movieTitlesList = movieTitlesList;
    }

    public PushNotificationsMovieInfo() {
        this.title = "Some of your favorite movies changes";
        this.movieTitlesList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addMoiveTitleItemToList(String movieTitle) {
        this.movieTitlesList.add(movieTitle);
    }

    public List<String> getMovieTitlesList() {
        return movieTitlesList;
    }

    public void setMovieTitlesList(List<String> movieTitlesList) {
        this.movieTitlesList = movieTitlesList;
    }
}
