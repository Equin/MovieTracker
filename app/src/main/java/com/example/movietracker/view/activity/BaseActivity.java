package com.example.movietracker.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.movietracker.AndroidApplication;
import com.example.movietracker.R;
import com.example.movietracker.listener.OnBackPressListener;

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

        AndroidApplication.setRunningActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("onDestroy", "destroyed");

        if (this.equals(AndroidApplication.getRunningActivity())) {
            AndroidApplication.setRunningActivity(null);
        }
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.container_for_fragment);
        if ((fragment instanceof OnBackPressListener) && !((OnBackPressListener)fragment).canGoBackOnBackPressed()) {
            return;
        }

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
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case ADD:
                fragmentTransaction.add(containerViewId, fragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            default:
                Log.d("TAG", "There is no such action for transaction : " + fragmentAction.toString());
        }
    }
}
