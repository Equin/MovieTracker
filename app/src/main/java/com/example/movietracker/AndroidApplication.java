package com.example.movietracker;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.di.DataProvider;
import com.squareup.leakcanary.LeakCanary;

public class AndroidApplication extends Application {

    private static Activity runningActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        this.initializeLeakDetection();
    }

    public static Activity getRunningActivity() {
        return AndroidApplication.runningActivity;
    }

    public static void setRunningActivity(Activity runningActivity) {
        AndroidApplication.runningActivity = runningActivity;
    }

    private void initializeLeakDetection() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
    }
}
