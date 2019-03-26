package com.example.movietracker.view.fragment.movie_details;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieReviewsEntity;
import com.example.movietracker.data.entity.MovieVideosEntity;
import com.example.movietracker.presenter.MovieDetailsTabLayoutPresenter;
import com.example.movietracker.view.adapter.ReviewListAdapter;
import com.example.movietracker.view.adapter.VideoListAdapter;
import com.example.movietracker.view.contract.TabLayoutView;
import com.example.movietracker.view.fragment.BaseFragment;
import com.example.movietracker.view.helper.FullScreenHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieVideoTabFragment extends BaseFragment implements TabLayoutView<MovieVideosEntity> {

    public interface MovieVideoTabFragmentInteractionListener {
        void showYouTubePlayer(String videoId);
    }


    public static MovieVideoTabFragment newInstance(int movieId) {
        MovieVideoTabFragment movieVideoTabFragment = new MovieVideoTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MovieDetailsFragment.ARG_SELECTED_MOVIE_ID, movieId);
        movieVideoTabFragment.setArguments(bundle);
        return movieVideoTabFragment;
    }

    public MovieVideoTabFragment() {
        setRetainInstance(true);
    }

    private MovieVideoTabFragmentInteractionListener movieVideoTabFragmentInteractionListener;
    private MovieDetailsTabLayoutPresenter movieDetailsTabLayoutPresenter;
    private FullScreenHelper fullScreenHelper;

    @BindView(R.id.recyclerView_videoList)
    RecyclerView recyclerViewVideo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_video_tab_item, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.movieDetailsTabLayoutPresenter = new MovieDetailsTabLayoutPresenter();

        this.movieDetailsTabLayoutPresenter.setView(this);
        this.movieDetailsTabLayoutPresenter.initialize();
        this.movieDetailsTabLayoutPresenter.getMovieVideos(getMovieIdFromArguments());

        this.fullScreenHelper = new FullScreenHelper(getActivity());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MovieVideoTabFragmentInteractionListener) {
            this.movieVideoTabFragmentInteractionListener = (MovieVideoTabFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.movieVideoTabFragmentInteractionListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.movieDetailsTabLayoutPresenter.destroy();
    }

    @Override
    public void showToast(int resourceId) {

    }

    private MovieVideosEntity someMovieData;

    @Override
    public void renderInfoToTab(MovieVideosEntity someMovieData) {
        this.someMovieData = someMovieData;
        RecyclerView.LayoutManager rowLayoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);
        recyclerViewVideo.setLayoutManager(rowLayoutManager);
        VideoListAdapter reviewListAdapter = new VideoListAdapter(someMovieData, getLifecycle(), new YouTubePlayerFullScListener());
        recyclerViewVideo.setAdapter(reviewListAdapter);
    }

    private int getMovieIdFromArguments() {
        if(getArguments() != null) {
            return   getArguments().getInt(MovieDetailsFragment.ARG_SELECTED_MOVIE_ID);
        }

        return -1;
    }

   private class YouTubePlayerFullScListener implements YouTubePlayerFullScreenListener {
        @Override
        public void onYouTubePlayerEnterFullScreen() {
            movieVideoTabFragmentInteractionListener.showYouTubePlayer(someMovieData.getMovieVideoResultEntities().get(0).getVideoKey());
            //fullScreenHelper.enterFullScreen();


        }

        @Override
        public void onYouTubePlayerExitFullScreen() {
           /// getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getActivity().getSupportFragmentManager().popBackStack();
           // fullScreenHelper.exitFullScreen();

        }
    }
}
