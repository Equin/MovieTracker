package com.example.movietracker.data.database;

import android.content.Context;

import com.example.movietracker.data.database.dao.GenresDao;
import com.example.movietracker.data.database.dao.MovieDao;
import com.example.movietracker.data.database.dao.MovieDetailDao;
import com.example.movietracker.data.database.dao.UserDao;
import com.example.movietracker.data.entity.MovieResultEntity;
import com.example.movietracker.data.entity.UserEntity;
import com.example.movietracker.data.entity.UserWithFavoriteMovies;
import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.MovieWithGenres;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastResultEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewResultEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideoResultEntity;

import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {
        GenreEntity.class,
        MovieResultEntity.class,
        MovieCastResultEntity.class,
        MovieReviewResultEntity.class,
        MovieVideoResultEntity.class,
        MovieDetailsEntity.class,
        MovieWithGenres.class,
        UserEntity.class,
        UserWithFavoriteMovies.class}, version = 1)
@TypeConverters(MyConverters.class)
public abstract class MoviesDatabase extends RoomDatabase {

    private static MoviesDatabase INSTANCE;
    private static final String DB_NAME = "MovieTrackerDB";

    public static MoviesDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (MoviesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MoviesDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(
                                            () ->  {
                                                getDatabase(context).getUserDao().saveUser(UserEntity.initialUser());
                                            });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract GenresDao getGenresDao();
    public abstract MovieDao getMovieDao();
    public abstract MovieDetailDao getMovieDetailDao();
    public abstract UserDao getUserDao();
}