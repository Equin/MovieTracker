package com.example.movietracker.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.view.fragment.MainFragment;
import com.example.movietracker.view.fragment.MovieListFragment;

import androidx.annotation.Nullable;

public class MainActivity extends BaseActivity implements
        MainFragment.MainFragmentInteractionListener  {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_fragment);

        if (savedInstanceState == null) {
            replaceFragment(R.id.container_for_fragment, MainFragment.newInstance());
        }
    }

    @Override
    public void showMovieListScreen(GenresEntity genresEntity) {
        replaceFragment(R.id.container_for_fragment, MovieListFragment.newInstance(genresEntity));
    }
}
