package com.example.movietracker.data.net;

import com.example.movietracker.data.date.DateTypeDeserializer;
import com.example.movietracker.data.net.api.MovieApi;
import com.example.movietracker.data.net.interceptor.QueryParameterInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.util.Date;

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

    @Override
    public void init(String baseUrl) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new QueryParameterInterceptor())
                .build();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateTypeDeserializer());
        Gson gson = gsonBuilder.create();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Override
    public MovieApi getMovieApi() {
        return retrofit.create(MovieApi.class);
    }
}
