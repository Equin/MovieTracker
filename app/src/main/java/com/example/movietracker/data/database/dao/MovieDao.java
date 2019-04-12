package com.example.movietracker.data.database.dao;

import com.example.movietracker.data.entity.MovieResultEntity;
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
            "INNER JOIN MovieWithGenres " +
            "ON MovieResultEntity.movieId = MovieWithGenres.movie_id " +
            "WHERE MovieWithGenres.genre_id in (:genresId) " +
            "AND (isAdult = 0 OR isAdult = (:isAdult))" +
            "GROUP BY movieId " +
            "ORDER BY moviePopularity DESC " +
            "LIMIT (:limit)"
    )
    Observable<List<MovieResultEntity>> getMoviesForPages(List<Integer> genresId, int limit, boolean isAdult);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM MovieResultEntity " +
            "INNER JOIN MovieWithGenres " +
            "ON MovieResultEntity.movieId = MovieWithGenres.movie_id " +
            "WHERE MovieWithGenres.genre_id in (:genresId) " +
            "AND (isAdult = 0 OR isAdult = (:isAdult))" +
            "GROUP BY movieId " +
            "ORDER BY moviePopularity DESC " +
            "LIMIT (:limit) OFFSET (:offset)"
    )
    Observable<List<MovieResultEntity>> getMovies(List<Integer> genresId, int limit, int offset, boolean isAdult);

    @Query("SELECT count(movieId) FROM MovieResultEntity " +
            "INNER JOIN MovieWithGenres " +
            "ON MovieResultEntity.movieId = MovieWithGenres.movie_id " +
            "WHERE MovieWithGenres.genre_id in (:genresId)"+
            "AND isAdult = 0 OR isAdult = (:isAdult)"
    )
    int getTotalResults(List<Integer> genresId, boolean isAdult);


    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM MovieResultEntity " +
            "WHERE movieId = (:movieId) " +
            "AND (isAdult = 0 or isAdult = (:isAdult))" +
            "GROUP BY movieId"
    )
    MovieResultEntity getMovieById(int movieId, boolean isAdult);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMovies(List<MovieResultEntity> moviesEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveRelation(MovieWithGenres movieWithGenres);

    default void addMovieGenreRelation(List<MovieResultEntity> movieResultEntities) {
        for(MovieResultEntity movieResultEntity : movieResultEntities)
        for(int genreId : movieResultEntity.getGenreIds()) {
            saveRelation(new MovieWithGenres(movieResultEntity.getMovieId(), genreId));
        }
    }
}