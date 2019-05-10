package com.example.movietracker.view.activity;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.movietracker.AndroidApplication;
import com.example.movietracker.R;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.listener.OnBackPressListener;
import com.example.movietracker.service.DownloadCompleteBroadcastReceiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getCanonicalName();

    private enum FragmentAction {
        ADD,
        REPLACE,
        REPLACE_WITHOUT_STACK
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);


        AndroidApplication.setRunningActivity(this);
        ClassProvider.initialize(getApplication().getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        AndroidApplication.setRunningActivity(this);
        ClassProvider.initialize(getApplication().getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "destroyed");

        if (this.equals(AndroidApplication.getRunningActivity())) {
            AndroidApplication.setRunningActivity(null);
            ClassProvider.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.container_for_fragment);
        Fragment fragmentContainer = this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if ((fragment instanceof OnBackPressListener) && !((OnBackPressListener)fragment).canGoBackOnBackPressed()) {
            return;
        }

        if(fragmentContainer != null) {
            if(!getSupportFragmentManager().popBackStackImmediate("MovieListFragment",0)) {
                if(!getSupportFragmentManager().popBackStackImmediate("MovieListFavoritesFragment",0 )) {
                  getSupportFragmentManager().popBackStack(
                          getSupportFragmentManager().getBackStackEntryAt(0).getId(),
                          POP_BACK_STACK_INCLUSIVE);
                }
            }
            return;
        }

        if (this.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }  else {
            super.onBackPressed();
        }
    }

    protected void addFragment(int containerViewId, Fragment fragment) {
        setFragment(containerViewId, fragment, FragmentAction.ADD, "");
    }

    protected void replaceFragment(int containerViewId, Fragment fragment, String fragmentName) {
        setFragment(containerViewId, fragment, FragmentAction.REPLACE, fragmentName);
    }

    protected void replaceFragmentWithoutStack(int containerViewId, Fragment fragment) {
        setFragment(containerViewId, fragment, FragmentAction.REPLACE_WITHOUT_STACK, "");
    }

    private void setFragment(
            final int containerViewId,
            final Fragment fragment,
            final FragmentAction fragmentAction,
            String fragmentName) {
        new Handler().post(() -> setFragmentAux(containerViewId, fragment, fragmentAction, fragmentName));
    }

    private void setFragmentAux(int containerViewId, Fragment fragment, FragmentAction fragmentAction, String fragmentName) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (fragmentAction) {
            case REPLACE:
                fragmentTransaction.replace(containerViewId, fragment);
                fragmentTransaction.addToBackStack(fragmentName);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case REPLACE_WITHOUT_STACK:
                fragmentTransaction.replace(containerViewId, fragment);
                fragmentTransaction.commit();
                break;
            case ADD:
                fragmentTransaction.add(containerViewId, fragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            default:
                Log.d(TAG, "There is no such action for transaction : " + fragmentAction.toString());
        }
    }
}
