package com.example.movietracker.data.entity.movie;

import com.google.gson.annotations.SerializedName;

public class MarkMovieAsFavoriteRequestBodyEntity {

    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("media_id")
    private int mediaId;

    @SerializedName("favorite")
    private boolean favorite;


    public MarkMovieAsFavoriteRequestBodyEntity(String mediaType, int mediaId, boolean favorite) {
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.favorite = favorite;
    }

    public MarkMovieAsFavoriteRequestBodyEntity(String mediaType) {
        this.mediaType = mediaType;
        this.mediaId = 0;
        this.favorite = false;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
