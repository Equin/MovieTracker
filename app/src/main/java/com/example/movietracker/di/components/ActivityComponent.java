package com.example.movietracker.di.components;

import android.app.Activity;

import com.example.movietracker.di.PerActivity;
import com.example.movietracker.di.modules.ActivityModule;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity activity();
}
