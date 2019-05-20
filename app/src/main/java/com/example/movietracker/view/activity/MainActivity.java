package com.example.movietracker.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.view.fragment.MainFragment;
import com.example.movietracker.view.fragment.movie_details.MovieDetailsFragment;
import com.example.movietracker.view.fragment.MovieListFragment;
import com.example.movietracker.view.fragment.movie_details.MovieVideoTabFragment;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends BaseActivity implements
        MainFragment.MainFragmentInteractionListener,
        MovieListFragment.MovieListFragmentInteractionListener,
        MovieVideoTabFragment.MovieVideoTabFragmentInteractionListener,
        MovieDetailsFragment.MovieDetailsFragmentInteractionListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_for_fragment);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }

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
        replaceFragment(R.id.container_for_fragment, MovieListFragment.newInstance(genresEntity, false), "MovieListFragment");
    }

    @Override
    public void showFavoriteMovieListScreen(GenresEntity genresEntity) {
        replaceFragment(R.id.container_for_fragment, MovieListFragment.newInstance(genresEntity, true), "MovieListFavoritesFragment");
    }

    @Override
    public void showMovieDetailScreen(int movieId) {
        replaceFragment(R.id.container_for_fragment, MovieDetailsFragment.newInstance(movieId), "MovieDetailsFragment");
    }

    @Override
    public void showYouTubePlayer(String videoId, MovieVideosEntity movieVideosEntity) {
        startActivity(YouTubeActivity.getCallingIntent(this, videoId, movieVideosEntity));
    }

    @Override
    public void openNewFragmentInTab(Fragment fragment, String fragmentName) {
            replaceFragment(R.id.fragment_container, fragment, fragmentName);
    }
}
