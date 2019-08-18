package com.example.movietracker.data.entity.movie;

import com.google.gson.annotations.SerializedName;

public class MarkMovieAsFavoriteResultEntity {

    @SerializedName("status_message")
    private String statusMessage;

    @SerializedName("status_code")
    private int statusCode;

    public MarkMovieAsFavoriteResultEntity() {
    }

    public MarkMovieAsFavoriteResultEntity(String statusMessage, int statusCode) {
        this.statusMessage = statusMessage;
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
