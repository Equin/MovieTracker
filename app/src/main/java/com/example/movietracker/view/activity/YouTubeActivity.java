package com.example.movietracker.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.view.fragment.movie_details.YouTubePlayerFragment;

import androidx.annotation.Nullable;

public class YouTubeActivity extends BaseActivity {

    private static final String EXTRA_VIDEO_ID = "extra_video_id";
    private static final String EXTRA_VIDEO_ENTITY = "extra_video_entity";

    public static Intent getCallingIntent(Context context, String videoId, MovieVideosEntity movieVideosEntity) {
        Intent intent = new Intent(context, YouTubeActivity.class);
        intent.putExtra(EXTRA_VIDEO_ID, videoId);
        intent.putExtra(EXTRA_VIDEO_ENTITY, movieVideosEntity);
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
            addFragment(R.id.container_for_fragment, YouTubePlayerFragment.newInstance(getVideoId(), getVideosList()));
        }
    }

    private String getVideoId() {
        return getIntent() != null ? getIntent().getStringExtra(EXTRA_VIDEO_ID) : "";
    }

    private MovieVideosEntity getVideosList() {
        return getIntent() != null ? (MovieVideosEntity)getIntent().getSerializableExtra(EXTRA_VIDEO_ENTITY) : null;
    }
}
