package com.example.movietracker.data.database.dao;

import com.example.movietracker.data.entity.genre.GenreEntity;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Flowable;
import io.reactivex.Observable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface GenresDao {

    @Query("SELECT * FROM GenreEntity")
    Observable<List<GenreEntity>> getAllGenres();

    @Insert(onConflict = REPLACE)
    void saveGenres(List<GenreEntity> genresEntity);
}
