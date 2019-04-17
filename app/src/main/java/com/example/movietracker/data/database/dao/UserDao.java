package com.example.movietracker.data.database.dao;

import com.example.movietracker.data.entity.MovieResultEntity;
import com.example.movietracker.data.entity.UserEntity;
import com.example.movietracker.data.entity.UserWithFavoriteMovies;

import java.util.List;
import java.util.concurrent.Executors;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface UserDao {
    @Query("SELECT * FROM UserEntity WHERE userId = 1")
    Observable<UserEntity> getUser();

    @Query("SELECT * FROM UserEntity WHERE userId = 1")
    UserEntity getUserNotObservable();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM UserEntity INNER JOIN userwithfavoritemovies ON user_id = userId WHERE userId = 1")
    Observable<List<UserEntity>> getUserWithFavorites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(UserEntity user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updateUser(UserEntity user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveRelation(UserWithFavoriteMovies userWithFavoriteMovies);

    @Delete
    Completable deleteMovieFromFavorites(UserWithFavoriteMovies userWithFavoriteMovies);

    default void addUserFavoriteMoviesRelation(UserEntity userEntity) {
        for(MovieResultEntity movieResult: userEntity.getFavoriteMovies()) {
            Executors.newSingleThreadScheduledExecutor().execute(
                    () ->  saveRelation(new UserWithFavoriteMovies(movieResult.getMovieId(), userEntity.getUserId())));
        }
    }
}
