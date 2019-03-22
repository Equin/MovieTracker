package com.example.movietracker;

import android.app.Activity;
import android.app.Application;

import com.example.movietracker.di.components.ApplicationComponent;
import com.example.movietracker.di.components.DaggerApplicationComponent;
import com.example.movietracker.di.modules.ApplicationModule;

public class AndroidApplication extends Application {
    private ApplicationComponent applicationComponent;

    private Activity runningActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    public Activity getRunningActivity() {
        return this.runningActivity;
    }

    public void setRunningActivity(Activity runningActivity) {
        this.runningActivity = runningActivity;
    }
}
