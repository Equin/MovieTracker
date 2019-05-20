package com.example.movietracker.data.entity.user;

import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

public class UserDetailsEntity {

    @SerializedName("id")
    private int userId;

    @SerializedName("avatar")
    private Avatar avatar;

    @SerializedName("name")
    private String name;

    @SerializedName("include_adult")
    private boolean includeAdult;

    @SerializedName("username")
    private String username;

    public UserDetailsEntity(int userId, Avatar avatar, String name, boolean includeAdult, String username) {
        this.userId = userId;
        this.avatar = avatar;
        this.name = name;
        this.includeAdult = includeAdult;
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIncludeAdult() {
        return includeAdult;
    }

    public void setIncludeAdult(boolean includeAdult) {
        this.includeAdult = includeAdult;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private class Avatar {

        @SerializedName("gravatar")
        Gravatar gravatar;

        public Avatar(Gravatar gravatar) {
            this.gravatar = gravatar;
        }

        public Gravatar getGravatar() {
            return gravatar;
        }

        public void setGravatar(Gravatar gravatar) {
            this.gravatar = gravatar;
        }

        private class Gravatar {
            @SerializedName("hash")
            private String hash;
        }
    }
}
