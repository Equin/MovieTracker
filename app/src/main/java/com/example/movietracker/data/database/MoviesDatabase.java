package com.example.movietracker.data.database;

import android.content.Context;

import com.example.movietracker.data.database.dao.GenresDao;
import com.example.movietracker.data.database.dao.MovieDao;
import com.example.movietracker.data.database.dao.MovieDetailDao;
import com.example.movietracker.data.entity.MovieResultEntity;
import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.MovieWithGenres;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastResultEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewResultEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideoResultEntity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {
        GenreEntity.class,
        MovieResultEntity.class,
        MovieCastResultEntity.class,
        MovieReviewResultEntity.class,
        MovieVideoResultEntity.class,
        MovieDetailsEntity.class,
        MovieWithGenres.class}, version = 1)
@TypeConverters(MyConverters.class)
public abstract class MoviesDatabase extends RoomDatabase {

    private static MoviesDatabase INSTANCE;
    private static final String DB_NAME = "GenreEntity";

    public static MoviesDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (MoviesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MoviesDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract GenresDao genresDao();
    public abstract MovieDao movieDao();
    public abstract MovieDetailDao movieDetailDao();
}