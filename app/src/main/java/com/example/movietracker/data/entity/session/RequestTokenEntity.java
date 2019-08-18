package com.example.movietracker.data.entity.session;

import com.google.gson.annotations.SerializedName;

public class RequestTokenEntity {

    @SerializedName("success")
    private boolean success;

    @SerializedName("expires_at")
    private String expiresAt;

    @SerializedName("request_token")
    private String requestToken;


    public RequestTokenEntity(boolean success, String expiresAt, String requestToken) {
        this.success = success;
        this.expiresAt = expiresAt;
        this.requestToken = requestToken;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }
}
