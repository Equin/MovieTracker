package com.example.movietracker.data.database.dao;

import com.example.movietracker.data.entity.MovieResultEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Observable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movieresultentity")
    Observable<List<MovieResultEntity>> getMoviesByOptions();

    @Insert(onConflict = REPLACE)
    void saveMovies(List<MovieResultEntity> moviesEntity);

}


    //AND sortBy = :sortBy AND page = :page AND isAdult = :isIncludeAdult
  /*  String sortBy,
    int page,
    boolean isIncludeAdult*/