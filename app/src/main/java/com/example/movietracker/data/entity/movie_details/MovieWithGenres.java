package com.example.movietracker.data.entity.movie_details;

import com.example.movietracker.data.entity.MovieResultEntity;
import com.example.movietracker.data.entity.genre.GenreEntity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(primaryKeys = {"movie_id", "genre_id"},
        indices = {@Index("movie_id"), @Index("genre_id")},
        foreignKeys = {
                @ForeignKey(entity = MovieResultEntity.class,
        parentColumns ="movieId",
        childColumns ="movie_id"),
                 @ForeignKey(entity=GenreEntity.class,
        parentColumns="genreId",
        childColumns="genre_id")
        })
public class MovieWithGenres {
    @ColumnInfo(name = "movie_id")
    private int movieId;

    @ColumnInfo(name = "genre_id")
    private int genreId;

    public MovieWithGenres(int movieId, int genreId) {
        this.movieId = movieId;
        this.genreId = genreId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieDetailId(int movieId) {
        this.movieId = movieId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

}
