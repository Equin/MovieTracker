package com.example.movietracker.data.entity.user;

import java.util.ArrayList;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.movietracker.data.entity.movie.MovieResultEntity;

@Entity
public class UserEntity {

    @PrimaryKey
    private int userId;
    private String parentalControlPassword;
    private boolean isParentalControlEnabled;
    @Ignore
    private List<MovieResultEntity> favoriteMovies;
    private String masterPassword;
    private boolean isBackgroundSyncEnabled;
    private boolean isGuestUser;
    private String TMDBPassword;
    private String TMDBUsername;
    private boolean isHasOpenSession;
    private String sessionId;

    @ColumnInfo(name = "movie_id")
    private int movieId;

    public UserEntity() {
    }

    public UserEntity(int userId, String parentalControlPassword, boolean isParentalControlEnabled, List<MovieResultEntity> favoriteMovies, String masterPassword, boolean isBackgroundSyncEnabled, boolean isGuestUser, String TMDBPassword, String TMDBUsername, String sessionId, boolean isHasOpenSession, int movieId) {
        this.userId = userId;
        this.parentalControlPassword = parentalControlPassword;
        this.isParentalControlEnabled = isParentalControlEnabled;
        this.favoriteMovies = favoriteMovies;
        this.masterPassword = masterPassword;
        this.isBackgroundSyncEnabled = isBackgroundSyncEnabled;
        this.isGuestUser = isGuestUser;
        this.TMDBPassword = TMDBPassword;
        this.TMDBUsername = TMDBUsername;
        this.sessionId = sessionId;
        this.movieId = movieId;
        this.isHasOpenSession = isHasOpenSession;
    }

    public String getParentalControlPassword() {
        return parentalControlPassword;
    }

    public void setParentalControlPassword(String parentalControlPassword) {
        this.parentalControlPassword = parentalControlPassword;
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
        return new UserEntity(1, null, false, new ArrayList<>(), "4546", true, false, "", "", "", false,  -1);
    }
    public static UserEntity initialGuestUser() {
        return new UserEntity(1, null, false, new ArrayList<>(), "4546", true, true, "", "Guest", "", false,  -1);
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

    public boolean isBackgroundSyncEnabled() {
        return isBackgroundSyncEnabled;
    }

    public void setBackgroundSyncEnabled(boolean backgroundSyncEnabled) {
        isBackgroundSyncEnabled = backgroundSyncEnabled;
    }

    public boolean isGuestUser() {
        return isGuestUser;
    }

    public void setGuestUser(boolean guestUser) {
        isGuestUser = guestUser;
    }

    public String getTMDBPassword() {
        return TMDBPassword;
    }

    public void setTMDBPassword(String TMDBPassword) {
        this.TMDBPassword = TMDBPassword;
    }

    public String getTMDBUsername() {
        return TMDBUsername;
    }

    public void setTMDBUsername(String TMDBUsername) {
        this.TMDBUsername = TMDBUsername;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isHasOpenSession() {
        return isHasOpenSession;
    }

    public void setHasOpenSession(boolean hasOpenSession) {
        this.isHasOpenSession = hasOpenSession;
    }
}
