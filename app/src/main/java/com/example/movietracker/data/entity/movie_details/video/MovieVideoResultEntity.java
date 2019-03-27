package com.example.movietracker.data.entity.movie_details.video;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MovieVideoResultEntity implements Serializable {

    @SerializedName("id")
    private String videoId;

    @SerializedName("iso_639_1")
    private String iso639;

    @SerializedName("iso_3166_1")
    private String iso3166;

    @SerializedName("key")
    private String videoKey;

    @SerializedName("name")
    private String videoName;

    @SerializedName("site")
    private String videoSite;

    @SerializedName("size")
    private int videoSize;

    @SerializedName("type")
    private String videoType;

    public MovieVideoResultEntity(String videoId, String iso639, String iso3166, String videoKey, String videoName, String videoSite, int videoSize, String videoType) {
        this.videoId = videoId;
        this.iso639 = iso639;
        this.iso3166 = iso3166;
        this.videoKey = videoKey;
        this.videoName = videoName;
        this.videoSite = videoSite;
        this.videoSize = videoSize;
        this.videoType = videoType;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getIso639() {
        return iso639;
    }

    public void setIso639(String iso639) {
        this.iso639 = iso639;
    }

    public String getIso3166() {
        return iso3166;
    }

    public void setIso3166(String iso3166) {
        this.iso3166 = iso3166;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoSite() {
        return videoSite;
    }

    public void setVideoSite(String videoSite) {
        this.videoSite = videoSite;
    }

    public int getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(int videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }
}
