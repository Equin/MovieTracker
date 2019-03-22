package com.example.movietracker.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.movietracker.R;
import com.example.movietracker.di.HasComponent;
import com.example.movietracker.di.components.CoreComponent;
import com.example.movietracker.di.components.DaggerCoreComponent;
import com.example.movietracker.di.modules.CoreModule;
import com.example.movietracker.view.fragment.MainFragment;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements HasComponent<CoreComponent>,
        MainFragment.MainFragmentInteractionListener  {


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    private CoreComponent coreComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_fragment);

        initializeInjector();

        if (savedInstanceState == null) {
            replaceFragment(R.id.container_for_fragment, MainFragment.newInstance());
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
