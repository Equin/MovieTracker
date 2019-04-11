package com.example.movietracker.data.entity;

import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class UserEntity {

    @PrimaryKey
    private int userId;
    private String pinCode;
    private boolean isParentalControlEnabled;
    @Ignore
    private List<MovieResultEntity> favoriteMoviesIds;
    private String masterPinCode;

    public UserEntity() {
    }

    public UserEntity(int userId, String pinCode, boolean isParentalControlEnabled, List<MovieResultEntity> favoriteMoviesIds, String masterPinCode) {
        this.userId = userId;
        this.pinCode = pinCode;
        this.isParentalControlEnabled = isParentalControlEnabled;
        this.favoriteMoviesIds = favoriteMoviesIds;
        this.masterPinCode = masterPinCode;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public boolean isParentalControlEnabled() {
        return isParentalControlEnabled;
    }

    public void setParentalControlEnabled(boolean parentalControlEnabled) {
        isParentalControlEnabled = parentalControlEnabled;
    }

    public List<MovieResultEntity> getFavoriteMoviesIds() {
        return favoriteMoviesIds;
    }

    public void setFavoriteMoviesIds(List<MovieResultEntity> favoriteMoviesIds) {
        this.favoriteMoviesIds = favoriteMoviesIds;
    }

    public String getMasterPinCode() {
        return masterPinCode;
    }

    public void setMasterPinCode(String masterPinCode) {
        this.masterPinCode = masterPinCode;
    }

    public static UserEntity initialUser() {
        return new UserEntity(1, null, false, null, "4546");
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
