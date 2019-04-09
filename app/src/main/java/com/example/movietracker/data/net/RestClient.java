package com.example.movietracker.data.net;

import com.example.movietracker.data.net.api.MovieApi;

public interface RestClient {
    void init(String baseUrl);
    MovieApi getMovieApi();
}
