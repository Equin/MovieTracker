package com.example.movietracker.di.components;

import android.content.Context;

import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.di.modules.ApplicationModule;
import com.example.movietracker.view.activity.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseActivity activity);

    Context context();
    MovieRepository movieRepository();
}
