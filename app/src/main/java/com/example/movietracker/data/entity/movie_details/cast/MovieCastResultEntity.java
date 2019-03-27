package com.example.movietracker.data.entity.movie_details.cast;

import com.google.gson.annotations.SerializedName;

public class MovieCastResultEntity {

    @SerializedName("cast_id")
    private int genreId;

    @SerializedName("name")
    private String castName;

    @SerializedName("profile_path")
    private String castImagePath;

    @SerializedName("order")
    private int castOrder;

    public MovieCastResultEntity(int genreId, String castName, String castImagePath, int castOrder) {
        this.genreId = genreId;
        this.castName = castName;
        this.castImagePath = castImagePath;
        this.castOrder = castOrder;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getCastName() {
        return castName;
    }

    public void setCastName(String castName) {
        this.castName = castName;
    }

    public String getCastImagePath() {
        return castImagePath;
    }

    public void setCastImagePath(String castImagePath) {
        this.castImagePath = castImagePath;
    }

    public int getCastOrder() {
        return castOrder;
    }

    public void setCastOrder(int castOrder) {
        this.castOrder = castOrder;
    }
}
