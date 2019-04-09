package com.example.movietracker.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieFilter;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.view.fragment.MainFragment;
import com.example.movietracker.view.fragment.movie_details.MovieDetailsFragment;
import com.example.movietracker.view.fragment.MovieListFragment;
import com.example.movietracker.view.fragment.movie_details.MovieVideoTabFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainActivity extends BaseActivity implements
        MainFragment.MainFragmentInteractionListener,
        MovieListFragment.MovieListFragmentInteractionListener,
        MovieVideoTabFragment.MovieVideoTabFragmentInteractionListener,
        MovieDetailsFragment.MovieDetailsFragmentInteractionListener {

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showMovieListScreen(GenresEntity genresEntity) {
        replaceFragment(R.id.container_for_fragment, MovieListFragment.newInstance(genresEntity));
    }

    @Override
    public void showMovieDetailScreen(int movieId) {
        replaceFragment(R.id.container_for_fragment, MovieDetailsFragment.newInstance(movieId));
    }

    @Override
    public void showYouTubePlayer(String videoId, MovieVideosEntity movieVideosEntity) {
        startActivity(YouTubeActivity.getCallingIntent(this, videoId, movieVideosEntity));
    }

    @Override
    public void openNewFragmentInTab(Fragment fragment) {
        replaceFragmentWithoutStack(R.id.fragment_container, fragment);
    }
}
