package com.example.movietracker.di;

import android.content.Context;
import android.util.Log;

import com.example.movietracker.data.net.RestClient;
import com.example.movietracker.data.net.RestClientImpl;
import com.example.movietracker.data.net.constant.NetConstant;
import com.example.movietracker.data.repository.MovieDataRepository;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.interactor.genre.GetGenresUseCase;
import com.example.movietracker.interactor.genre.GetMoviesUseCase;
import com.example.movietracker.presenter.MainPresenter;
import com.example.movietracker.presenter.MovieListPresenter;
import com.example.movietracker.view.custom_view.GenreView;

public class ClassProvider {

    private static final String TAG = ClassProvider.class.getCanonicalName() ;

    private ClassProvider() {
    }

    public static RestClient restClient;
    public static MovieRepository movieRepository;
    public static MainPresenter mainPresenter;
    public static MovieListPresenter movieListPresenter;
    public static GetGenresUseCase getGenresUseCase;
    public static GetMoviesUseCase getMoviesUseCase;

    public static GenreView genreView;

    public static void initialize(Context context) {

        restClient = RestClientImpl.getInstance();
        ((RestClientImpl) restClient).init(NetConstant.BASE_URL);

        movieRepository = MovieDataRepository.getInstance();
        ((MovieDataRepository) movieRepository).init(restClient);

        getGenresUseCase = new GetGenresUseCase(movieRepository);
        getMoviesUseCase = new GetMoviesUseCase(movieRepository);

        mainPresenter = new MainPresenter(getGenresUseCase);
        movieListPresenter = new MovieListPresenter(getMoviesUseCase);

        genreView = new GenreView(context);

        Log.d(TAG, "initialized");
    }

    public static void onDestroy() {
        restClient = null;
        movieRepository = null;
        getGenresUseCase = null;
        getMoviesUseCase =null;
        mainPresenter = null;
        movieListPresenter = null;
        genreView = null;

        Log.d(TAG, "destroyed");
    }
}
