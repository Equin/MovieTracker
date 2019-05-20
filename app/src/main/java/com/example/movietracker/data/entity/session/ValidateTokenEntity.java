package com.example.movietracker.data.entity.session;

import com.google.gson.annotations.SerializedName;

public class ValidateTokenEntity {

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("request_token")
    private String requestToken;

    public ValidateTokenEntity(String username, String password, String requestToken) {
        this.username = username;
        this.password = password;
        this.requestToken = requestToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }
}
