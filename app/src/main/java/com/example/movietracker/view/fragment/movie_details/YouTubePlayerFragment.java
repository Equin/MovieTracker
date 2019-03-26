package com.example.movietracker.view.fragment.movie_details;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieVideosEntity;
import com.example.movietracker.presenter.MovieDetailsTabLayoutPresenter;
import com.example.movietracker.view.adapter.VideoListAdapter;
import com.example.movietracker.view.contract.TabLayoutView;
import com.example.movietracker.view.fragment.BaseFragment;
import com.example.movietracker.view.helper.FullScreenHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class YouTubePlayerFragment extends BaseFragment {

    private static final String ARG_VIDEO_ID = "arg_video_id";

    public static YouTubePlayerFragment newInstance(String videoId) {
        YouTubePlayerFragment youTubePlayerFragment = new YouTubePlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_VIDEO_ID, videoId);
        youTubePlayerFragment.setArguments(bundle);
        return youTubePlayerFragment;
    }

    public YouTubePlayerFragment() {
        setRetainInstance(true);
    }

    private FullScreenHelper fullScreenHelper;

    @BindView(R.id.youtube_player_in_fragment)
    YouTubePlayerView youTubePlayerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.you_tube_player, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.fullScreenHelper = new FullScreenHelper(getActivity());
        initYouTubePlayerView();
    }

    private void initYouTubePlayerView() {

        getLifecycle().addObserver(youTubePlayerView);
        initPictureInPicture(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        getMovieIdFromArguments(),0f
                );
            }
        });
    }

    private void initPictureInPicture(YouTubePlayerView youTubePlayerView) {
        ImageView pictureInPictureIcon = new ImageView(getContext());
        pictureInPictureIcon.setImageDrawable(getContext().getDrawable(R.drawable.ic_picture_in_picture_24dp));

        pictureInPictureIcon.setOnClickListener( view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                boolean supportsPIP = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
                if(supportsPIP)
                    getActivity().enterPictureInPictureMode();
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle("Can't enter picture in picture mode")
                        .setMessage("In order to enter picture in picture mode you need a SDK version >= N.")
                        .show();
            }
        });

        youTubePlayerView.getPlayerUiController().addView(pictureInPictureIcon);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);

        if(isInPictureInPictureMode) {
            youTubePlayerView.enterFullScreen();
            youTubePlayerView.getPlayerUiController().showUi(false);
        } else {
            youTubePlayerView.exitFullScreen();
            youTubePlayerView.getPlayerUiController().showUi(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void showToast(int resourceId) {

    }

    private String getMovieIdFromArguments() {
        if(getArguments() != null) {
            return   getArguments().getString(ARG_VIDEO_ID);
        }

        return "";
    }
}
