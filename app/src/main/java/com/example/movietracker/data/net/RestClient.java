package com.example.movietracker.data.net;

import com.example.movietracker.data.net.api.AuthApi;
import com.example.movietracker.data.net.api.MovieApi;
import com.example.movietracker.data.net.api.UserApi;

public interface RestClient {
    void init(String baseUrl);
    MovieApi getMovieApi();
    AuthApi getAuthApi();
    UserApi getUserApi();
}
