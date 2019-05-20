package com.example.movietracker.data.entity.session;

import com.google.gson.annotations.SerializedName;

public class SessionEntity {

    @SerializedName("success")
    private boolean success;

    @SerializedName("session_id")
    private String sessionId;

    public SessionEntity(boolean success, String sessionId) {
        this.success = success;
        this.sessionId = sessionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
