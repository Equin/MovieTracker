package com.example.movietracker.data.database.dao;

import com.example.movietracker.data.entity.UserEntity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface UserDao {
    @Query("SELECT * FROM UserEntity WHERE userId = 1")
    Observable<UserEntity> getUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveUser(UserEntity user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updateUser(UserEntity user);


}
