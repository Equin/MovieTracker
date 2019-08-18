package com.example.movietracker.data.entity.movie;

import com.google.gson.annotations.SerializedName;

public class MovieChangesResultEntity {

    @SerializedName("id")
    private int id;

    @SerializedName("adult")
    private boolean isAdult;

    public MovieChangesResultEntity(int id, boolean isAdult) {
        this.id = id;
        this.isAdult = isAdult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }
}