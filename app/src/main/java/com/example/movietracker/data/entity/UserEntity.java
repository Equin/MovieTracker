package com.example.movietracker.data.entity;

import java.util.ArrayList;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class UserEntity {

    @PrimaryKey
    private int userId;
    private String password;
    private boolean isParentalControlEnabled;
    @Ignore
    private List<MovieResultEntity> favoriteMovies;
    private String masterPassword;

    @ColumnInfo(name = "movie_id")
    private int movieId;

    public UserEntity() {
    }

    public UserEntity(int userId, String password, boolean isParentalControlEnabled, List<MovieResultEntity> favoriteMovies, String masterPassword, int movieId) {
        this.userId = userId;
        this.password = password;
        this.isParentalControlEnabled = isParentalControlEnabled;
        this.favoriteMovies = favoriteMovies;
        this.masterPassword = masterPassword;
        this.movieId = movieId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isParentalControlEnabled() {
        return isParentalControlEnabled;
    }

    public void setParentalControlEnabled(boolean parentalControlEnabled) {
        isParentalControlEnabled = parentalControlEnabled;
    }

    public List<MovieResultEntity> getFavoriteMovies() {
        return favoriteMovies;
    }

    public void setFavoriteMovies(List<MovieResultEntity> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
    }

    public void addToFavorites(MovieResultEntity movieResultEntity) {
        if (favoriteMovies != null && !favoriteMovies.contains(movieResultEntity)) {
            favoriteMovies.add(movieResultEntity);
        } else {
            favoriteMovies = new ArrayList<>();
            favoriteMovies.add(movieResultEntity);
        }
    }

    public void removeFromFavorites(MovieResultEntity movieResultEntity) {
        if (favoriteMovies != null) {
            favoriteMovies.remove(movieResultEntity);
        }
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    public static UserEntity initialUser() {
        return new UserEntity(1, null, false, new ArrayList<>(), "4546", -1);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
