package com.example.movietracker.di;

import android.app.Application;
import android.util.Log;

import com.example.movietracker.AndroidApplication;
import com.example.movietracker.data.database.MoviesDatabase;
import com.example.movietracker.data.net.RestClient;
import com.example.movietracker.data.net.RestClientImpl;
import com.example.movietracker.data.net.constant.NetConstant;
import com.example.movietracker.data.repository.MovieDataRepository;
import com.example.movietracker.data.repository.MovieRepository;

public class ClassProvider {

    private static final String TAG = ClassProvider.class.getCanonicalName() ;

    private ClassProvider() {
    }

    public static RestClient restClient;
    public static MoviesDatabase moviesDatabase;
    public static MovieRepository movieRepository;

    public static void initialize() {

        restClient = RestClientImpl.getInstance();
        ((RestClientImpl) restClient).init(NetConstant.BASE_URL);

        moviesDatabase = MoviesDatabase.getDatabase(AndroidApplication.getRunningActivity().getBaseContext());

        movieRepository = MovieDataRepository.getInstance();
        ((MovieDataRepository) movieRepository).init(restClient, moviesDatabase);

        Log.d(TAG, "initialized");
    }

    public static void onDestroy() {
        restClient = null;
        movieRepository = null;
        moviesDatabase = null;

        Log.d(TAG, "destroyed");
    }
}
