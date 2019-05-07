package com.example.movietracker.view.fragment.movie_details;

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
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.listener.OnBackPressListener;
import com.example.movietracker.view.adapter.VideoListAdapter;
import com.example.movietracker.view.fragment.BaseFragment;
import com.example.movietracker.view.helper.FullScreenHelper;
import com.example.movietracker.listener.SnapScrollListener;
import com.example.movietracker.view.helper.RecyclerViewOrientationUtility;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class YouTubePlayerFragment extends BaseFragment implements OnBackPressListener {

    private static final String ARG_VIDEO_ID = "arg_video_id";
    private static final String ARG_VIDEO_ENTITY = "arg_video_entity";

    public static YouTubePlayerFragment newInstance(String videoId, MovieVideosEntity movieVideosEntity) {
        YouTubePlayerFragment youTubePlayerFragment = new YouTubePlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_VIDEO_ID, videoId);
        bundle.putSerializable(ARG_VIDEO_ENTITY, movieVideosEntity);
        youTubePlayerFragment.setArguments(bundle);
        return youTubePlayerFragment;
    }

    public YouTubePlayerFragment() {
        setRetainInstance(true);
    }

    private FullScreenHelper fullScreenHelper;
    private MovieVideosEntity movieVideosEntity;
    private YouTubePlayer youTubePlayer;

    @BindView(R.id.youtube_player_in_fragment)
    YouTubePlayerView youTubePlayerView;

    @BindView(R.id.recyclerView_youtube_video_list)
    RecyclerView recyclerViewYoutubeVideo;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.you_tube_player3, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hideLoading();
        this.fullScreenHelper = new FullScreenHelper(getActivity());
        this.movieVideosEntity = getVideosFromArguments();
        initYouTubePlayerView();
        initVideoListRecyclerView();
        setYouTubePlayerViewStateAccordingToConfiguration(getContext().getResources().getConfiguration().orientation);
    }

    private void initVideoListRecyclerView() {

        RecyclerViewOrientationUtility.setLayoutManagerToRecyclerView(
                this.recyclerViewYoutubeVideo,
                getContext().getResources().getConfiguration().orientation);

        VideoListAdapter videoListAdapter = new VideoListAdapter(this.movieVideosEntity, new ClickListener());
        this.recyclerViewYoutubeVideo.setAdapter(videoListAdapter);
        this.recyclerViewYoutubeVideo.addOnScrollListener(new SnapScrollListener(this));
    }

    private void initYouTubePlayerView() {

        getLifecycle().addObserver(youTubePlayerView);
        initPictureInPicture(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                YouTubePlayerFragment.this.youTubePlayer = youTubePlayer;
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        getVideoIdFromArguments(), 0f
                );
            }
        });

        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScListener());
    }

    private void playVideo(String videoId) {
        if(youTubePlayer!=null) {
            youTubePlayer.loadVideo(videoId, 0f);
        }
    }

    private void initPictureInPicture(YouTubePlayerView youTubePlayerView) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ImageView pictureInPictureIcon = new ImageView(getContext());
            pictureInPictureIcon.setImageDrawable(getContext().getDrawable(R.drawable.ic_picture_in_picture_24dp));

            pictureInPictureIcon.setOnClickListener(view -> {
                boolean supportsPIP = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
                if (supportsPIP)
                    getActivity().enterPictureInPictureMode();
            });

            youTubePlayerView.getPlayerUiController().addView(pictureInPictureIcon);
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);

        if (isInPictureInPictureMode) {
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

        this.fullScreenHelper = null;
        this.youTubePlayer = null;
        this.recyclerViewYoutubeVideo = null;
        this.youTubePlayerView = null;
    }

    private String getVideoIdFromArguments() {
        if (getArguments() != null) {
            return getArguments().getString(ARG_VIDEO_ID);
        }

        return "";
    }

    private MovieVideosEntity getVideosFromArguments() {
        if (getArguments() != null) {
            return (MovieVideosEntity) getArguments().getSerializable(ARG_VIDEO_ENTITY);
        }

        return null;
    }

    private void setYouTubePlayerViewStateAccordingToConfiguration(int orientation) {
        if(orientation == Configuration.ORIENTATION_LANDSCAPE && !youTubePlayerView.isFullScreen()) {
            youTubePlayerView.enterFullScreen();
            fullScreenHelper.enterFullScreen();
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT && youTubePlayerView.isFullScreen()){
            youTubePlayerView.exitFullScreen();
            fullScreenHelper.exitFullScreen();
        }
    }

    @Override
    public boolean canGoBackOnBackPressed() {
        if(this.youTubePlayerView.isFullScreen()) {
            this.youTubePlayerView.exitFullScreen();
            this.fullScreenHelper.exitFullScreen();
            return false;
        }

        return true;
    }

    private class YouTubePlayerFullScListener implements YouTubePlayerFullScreenListener {
        @Override
        public void onYouTubePlayerEnterFullScreen() {
            fullScreenHelper.enterFullScreen();
            rotateScreen(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        @Override
        public void onYouTubePlayerExitFullScreen() {
            fullScreenHelper.exitFullScreen();
            rotateScreen(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setYouTubePlayerViewStateAccordingToConfiguration(configuration.orientation);
        RecyclerViewOrientationUtility.setLayoutManagerToRecyclerView(this.recyclerViewYoutubeVideo, configuration.orientation);
    }

    private void rotateScreen(int orientation) {
        getActivity().setRequestedOrientation(orientation);

        getView().postDelayed(() ->
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR),  3000L);
    }

    private class ClickListener implements RecyclerView.OnClickListener {

        private int previousClick = -1;
        private View previousView;

        @Override
        public void onClick(View v) {

            int position = recyclerViewYoutubeVideo.getChildAdapterPosition(v);

            if(position != previousClick) {
                playVideo(movieVideosEntity.getMovieVideoResultEntities().get(position).getVideoKey());
            }

            if(previousClick != position && previousClick != -1) {
                v.setAlpha(0.5f);
                previousView.setAlpha(1f);;
                previousClick = position;
                previousView = v;
            } else {
                v.setAlpha(0.5f);
                previousView = v;
                previousClick = position;
            }
        }

    }
}