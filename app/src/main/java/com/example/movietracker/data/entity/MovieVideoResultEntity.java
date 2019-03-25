package com.example.movietracker.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideoResultEntity {

    @SerializedName("id")
    private int videoId;

    @SerializedName("iso_639_1")
    private int iso639;

    @SerializedName("iso_3166_1")
    private int iso3166;

    @SerializedName("key")
    private int videoKey;

    @SerializedName("name")
    private int videoName;

    @SerializedName("site")
    private int videoSite;

    @SerializedName("size")
    private int videoSize;

    @SerializedName("type")
    private int videoType;

    public MovieVideoResultEntity(int videoId, int iso639, int iso3166, int videoKey, int videoName, int videoSite, int videoSize, int videoType) {
        this.videoId = videoId;
        this.iso639 = iso639;
        this.iso3166 = iso3166;
        this.videoKey = videoKey;
        this.videoName = videoName;
        this.videoSite = videoSite;
        this.videoSize = videoSize;
        this.videoType = videoType;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getIso639() {
        return iso639;
    }

    public void setIso639(int iso639) {
        this.iso639 = iso639;
    }

    public int getIso3166() {
        return iso3166;
    }

    public void setIso3166(int iso3166) {
        this.iso3166 = iso3166;
    }

    public int getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(int videoKey) {
        this.videoKey = videoKey;
    }

    public int getVideoName() {
        return videoName;
    }

    public void setVideoName(int videoName) {
        this.videoName = videoName;
    }

    public int getVideoSite() {
        return videoSite;
    }

    public void setVideoSite(int videoSite) {
        this.videoSite = videoSite;
    }

    public int getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(int videoSize) {
        this.videoSize = videoSize;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

}
