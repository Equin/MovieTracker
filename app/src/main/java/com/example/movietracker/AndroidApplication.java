package com.example.movietracker;

import android.app.Activity;
import android.app.Application;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.di.DataProvider;
import com.squareup.leakcanary.LeakCanary;

public class AndroidApplication extends Application {

    private Activity runningActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        this.initializeLeakDetection();
    }

    private void initializeInjector() {
        ClassProvider.initialize();
        DataProvider.initialize();
    }

    public Activity getRunningActivity() {
        return this.runningActivity;
    }

    public void setRunningActivity(Activity runningActivity) {
        this.runningActivity = runningActivity;
    }

    private void initializeLeakDetection() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
    }
}
