package com.example.movietracker.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.movietracker.R;
import com.example.movietracker.di.HasComponent;
import com.example.movietracker.di.components.CoreComponent;
import com.example.movietracker.di.components.DaggerCoreComponent;
import com.example.movietracker.di.modules.CoreModule;
import com.example.movietracker.view.fragment.MainFragment;
import com.example.movietracker.view.fragment.MovieListFragment;

import androidx.annotation.Nullable;

public class MovieListActivity extends BaseActivity implements HasComponent<CoreComponent> {


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MovieListActivity.class);
    }

    private CoreComponent coreComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_fragment);

        initializeInjector();

        if (savedInstanceState == null) {
            replaceFragment(R.id.container_for_fragment, MovieListFragment.newInstance());
        }
    }

    private void initializeInjector() {
        this.coreComponent = DaggerCoreComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .coreModule(new CoreModule())
                .build();
    }

    @Override
    public CoreComponent getComponent() {
        if (this.coreComponent == null) {
            initializeInjector();
        }
        return this.coreComponent;
    }
}
