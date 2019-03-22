package com.example.movietracker.data.net;

import com.example.movietracker.data.net.api.MovieApi;
import com.example.movietracker.data.net.interceptor.QueryParameterInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClientImpl implements RestClient {

    private Retrofit retrofit;

    private RestClientImpl() {}

    private static class SingletonHelper {
        private static final RestClientImpl INSTANCE = new RestClientImpl();
    }

    public static RestClientImpl getInstance(){
        return SingletonHelper.INSTANCE;
    }

    public void init(String baseUrl) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new QueryParameterInterceptor())
                .build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Override
    public MovieApi getMovieApi() {
        return retrofit.create(MovieApi.class);
    }
}
