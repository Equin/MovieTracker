package com.example.movietracker.di.modules;

import android.content.Context;

import com.example.movietracker.AndroidApplication;
import com.example.movietracker.data.net.RestClient;
import com.example.movietracker.data.net.RestClientImpl;
import com.example.movietracker.data.net.constant.NetConstant;
import com.example.movietracker.data.repository.MovieDataRepository;
import com.example.movietracker.data.repository.MovieRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class ApplicationModule {

    private final AndroidApplication application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    RestClient provideRestClient() {
        return new RestClientImpl(NetConstant.BASE_URL);
    }


    @Provides
    @Singleton
    MovieRepository provideTeamRepository(RestClient restClient) {
        return new MovieDataRepository(restClient);
    }
}