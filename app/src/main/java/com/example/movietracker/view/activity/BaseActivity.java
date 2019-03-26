package com.example.movietracker.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.movietracker.AndroidApplication;
import com.example.movietracker.di.ClassProvider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public abstract class BaseActivity extends AppCompatActivity {

    private enum FragmentAction {
        ADD,
        REPLACE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getAndroidApplication().setRunningActivity(this);

        ClassProvider.initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("onDestroy", "destroyed");

        if (this.equals(getAndroidApplication().getRunningActivity())) {
            getAndroidApplication().setRunningActivity(null);
        }

        ClassProvider.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (this.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }  else {
            super.onBackPressed();
        }
    }

    protected void addFragment(int containerViewId, Fragment fragment) {
        setFragment(containerViewId, fragment, FragmentAction.ADD);
    }

    protected void replaceFragment(int containerViewId, Fragment fragment) {
        setFragment(containerViewId, fragment, FragmentAction.REPLACE);
    }

    private void setFragment(
            final int containerViewId,
            final Fragment fragment,
            final FragmentAction fragmentAction) {
        new Handler().post(() -> setFragmentAux(containerViewId, fragment, fragmentAction));
    }

    private void setFragmentAux(int containerViewId, Fragment fragment, FragmentAction fragmentAction) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (fragmentAction) {
            case REPLACE:
                fragmentTransaction.replace(containerViewId, fragment);
                fragmentTransaction.addToBackStack("replacedFragment");
                break;
            case ADD:
                fragmentTransaction.add(containerViewId, fragment);
               // fragmentTransaction.addToBackStack(null);
                break;
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    private AndroidApplication getAndroidApplication() {
        return ((AndroidApplication) getApplication());
    }
}
