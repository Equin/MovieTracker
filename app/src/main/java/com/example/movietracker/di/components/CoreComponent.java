package com.example.movietracker.di.components;

import com.example.movietracker.di.PerActivity;
import com.example.movietracker.di.modules.ActivityModule;
import com.example.movietracker.di.modules.CoreModule;
import com.example.movietracker.view.fragment.MainFragment;
import com.example.movietracker.view.fragment.MovieListFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, CoreModule.class})
public interface CoreComponent {
    void inject(MainFragment mainFragment);
    void inject(MovieListFragment movieListFragment);
}
