package com.example.movietracker.data.database.dao;

import com.example.movietracker.data.entity.MovieResultEntity;
import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.MovieWithGenres;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import io.reactivex.Observable;

@Dao
public interface MovieDao {

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM MovieResultEntity " +
            "INNER JOIN MovieWithGenres ON MovieResultEntity.movieId = MovieWithGenres.movie_id " +
            "WHERE MovieWithGenres.genre_id in (:genresId) " +
            "GROUP BY movieId " +
            "ORDER BY moviePopularity DESC")
    Observable<List<MovieResultEntity>> getMoviesByOptions(List<Integer> genresId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMovies(List<MovieResultEntity> moviesEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveRelation(MovieWithGenres movieWithGenres);

    default void addMovieGenreRelation(List<MovieResultEntity> movieResultEntities) {
        for(MovieResultEntity movieResultEntity : movieResultEntities)
        for(int genreId : movieResultEntity.getGenreIds()) {
            saveRelation(new MovieWithGenres(movieResultEntity.getMovieId(), genreId));
        }
    };
}


    //AND sortBy = :sortBy AND page = :page AND isAdult = :isIncludeAdult
  /*  String sortBy,
    int page,
    boolean isIncludeAdult*/