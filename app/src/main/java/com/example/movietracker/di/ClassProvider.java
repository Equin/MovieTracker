package com.example.movietracker.di;

import android.content.Context;
import android.util.Log;

import com.example.movietracker.data.database.MoviesDatabase;
import com.example.movietracker.data.net.RestClient;
import com.example.movietracker.data.net.RestClientImpl;
import com.example.movietracker.data.net.constant.NetConstant;
import com.example.movietracker.data.repository.AuthDataRepository;
import com.example.movietracker.data.repository.AuthRepository;
import com.example.movietracker.data.repository.MovieDataRepository;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.data.repository.UserDataRepository;
import com.example.movietracker.data.repository.UserRepository;
import com.example.movietracker.view.FilterAlertDialog;

public class ClassProvider {

    private static final String TAG = ClassProvider.class.getCanonicalName() ;

    private ClassProvider() {
    }

    public static RestClient restClient;
    public static MoviesDatabase moviesDatabase;
    public static MovieRepository movieRepository;
    public static UserRepository userRepository;
    public static AuthRepository authRepository;
    public static FilterAlertDialog filterAlertDialog;

    public static void initialize(Context context) {

        restClient = RestClientImpl.getInstance();
        restClient.init(NetConstant.BASE_URL);

        moviesDatabase = MoviesDatabase.getDatabase(context);

        userRepository = UserDataRepository.getInstance();
        userRepository.init(restClient, moviesDatabase);

        authRepository = AuthDataRepository.getInstance();
        authRepository.init(restClient, moviesDatabase, userRepository);

        movieRepository = MovieDataRepository.getInstance();
        movieRepository.init(restClient, moviesDatabase, userRepository);

        filterAlertDialog = FilterAlertDialog.getInstance();
        filterAlertDialog.init();

        Log.d(TAG, "initialized");
    }

    public static void onDestroy() {
        restClient = null;
        movieRepository = null;
        moviesDatabase = null;
        filterAlertDialog.deleteFilterAlertDialog();
        filterAlertDialog = null;

        Log.d(TAG, "destroyed");
    }
}
