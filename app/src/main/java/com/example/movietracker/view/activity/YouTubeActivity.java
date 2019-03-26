package com.example.movietracker.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.view.fragment.MainFragment;
import com.example.movietracker.view.fragment.MovieListFragment;
import com.example.movietracker.view.fragment.movie_details.MovieDetailsFragment;
import com.example.movietracker.view.fragment.movie_details.YouTubePlayerFragment;

import androidx.annotation.Nullable;

public class YouTubeActivity extends BaseActivity {

    private static final String EXTRA_VIDEO_ID = "extra_video_id";

    public static Intent getCallingIntent(Context context, String videoId) {
        Intent intent = new Intent(context, YouTubeActivity.class);
        intent.putExtra(EXTRA_VIDEO_ID, videoId);
        return intent;
    }


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, YouTubeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_fragment);

        if (savedInstanceState == null) {
            replaceFragment(R.id.container_for_fragment, YouTubePlayerFragment.newInstance(getVideoId()));
        }
    }

    private String getVideoId() {
        return getIntent() != null ? getIntent().getStringExtra(EXTRA_VIDEO_ID) : "";
    }
}
