package com.example.movietracker.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.example.movietracker.AndroidApplication;
import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieRequestEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.di.DataProvider;
import com.example.movietracker.view.fragment.MainFragment;
import com.example.movietracker.view.fragment.movie_details.MovieDetailsFragment;
import com.example.movietracker.view.fragment.MovieListFragment;
import com.example.movietracker.view.fragment.movie_details.MovieVideoTabFragment;

import androidx.annotation.Nullable;

public class MainActivity extends BaseActivity implements
        MainFragment.MainFragmentInteractionListener,
        MovieListFragment.MovieListFragmentInteractionListener,
        MovieVideoTabFragment.MovieVideoTabFragmentInteractionListener {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_fragment);

        if (savedInstanceState == null) {
            addFragment(R.id.container_for_fragment, MainFragment.newInstance());
        }

        DataProvider.initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataProvider.onDestroy();
    }

    @Override
    public void showMovieListScreen(MovieRequestEntity movieRequestEntity) {
        replaceFragment(R.id.container_for_fragment, MovieListFragment.newInstance(movieRequestEntity));
    }

    @Override
    public void showMovieDetailScreen(int movieId) {
        replaceFragment(R.id.container_for_fragment, MovieDetailsFragment.newInstance(movieId));
    }

    @Override
    public void showYouTubePlayer(String videoId, MovieVideosEntity movieVideosEntity) {
        startActivity(YouTubeActivity.getCallingIntent(this, videoId, movieVideosEntity));
    }
}
