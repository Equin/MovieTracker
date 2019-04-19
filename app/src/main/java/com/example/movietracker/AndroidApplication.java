package com.example.movietracker;

import android.app.Activity;
import android.app.Application;

import com.example.movietracker.service.UpdateMoviesFromServerWorker;
import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

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
