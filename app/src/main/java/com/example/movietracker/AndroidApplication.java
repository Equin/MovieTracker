package com.example.movietracker;

import android.app.Activity;
import android.app.Application;
import com.example.movietracker.di.ClassProvider;

public class AndroidApplication extends Application {

    private Activity runningActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
    }

    private void initializeInjector() {
        ClassProvider.initialize(getApplicationContext());
    }

    public Activity getRunningActivity() {
        return this.runningActivity;
    }

    public void setRunningActivity(Activity runningActivity) {
        this.runningActivity = runningActivity;
    }
}
