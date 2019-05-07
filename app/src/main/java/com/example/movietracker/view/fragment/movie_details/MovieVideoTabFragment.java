package com.example.movietracker.view.fragment.movie_details;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.presenter.MovieDetailsTabLayoutPresenter;
import com.example.movietracker.view.MovieCardItemDecorator;
import com.example.movietracker.view.adapter.VideoListAdapter;
import com.example.movietracker.view.contract.TabLayoutView;
import com.example.movietracker.view.fragment.BaseFragment;
import com.example.movietracker.listener.SnapScrollListener;
import com.example.movietracker.view.helper.RecyclerViewOrientationUtility;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieVideoTabFragment<V extends MovieVideosEntity> extends BaseFragment implements TabLayoutView<MovieVideosEntity> {

    private static final int RECYCLER_VIEW_CARD_ITEM_OFFSET_DPI = 4;

    public interface MovieVideoTabFragmentInteractionListener {
        void showYouTubePlayer(String videoId, MovieVideosEntity movieVideosEntity);
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

    @BindView(R.id.recyclerView_videoList)
    RecyclerView recyclerViewVideo;

    @BindView(R.id.scrollView_nothingToShow)
    NestedScrollView scrollViewNothingToShow;

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

        RecyclerViewOrientationUtility.setLayoutManagerToRecyclerView(
                this.recyclerViewVideo,
                getContext().getResources().getConfiguration().orientation);

        this.movieDetailsTabLayoutPresenter = new MovieDetailsTabLayoutPresenter(this);
        this.movieDetailsTabLayoutPresenter.getMovieVideos(getMovieIdFromArguments());
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
        this.recyclerViewVideo = null;
        this.scrollViewNothingToShow = null;
        this.movieVideoTabFragmentInteractionListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideLoading();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (  this.movieDetailsTabLayoutPresenter != null) {
            this.movieDetailsTabLayoutPresenter.destroy();
        }
    }

    private MovieVideosEntity someMovieData;

    @Override
    public void renderInfoToTab(MovieVideosEntity someMovieData) {
        this.someMovieData = someMovieData;

        VideoListAdapter reviewListAdapter = new VideoListAdapter(someMovieData, new ClickListener());
        this.recyclerViewVideo.setAdapter(reviewListAdapter);

        this.recyclerViewVideo.addItemDecoration(new MovieCardItemDecorator(RECYCLER_VIEW_CARD_ITEM_OFFSET_DPI));
        this.recyclerViewVideo.addOnScrollListener(new SnapScrollListener(this));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        RecyclerViewOrientationUtility.setLayoutManagerToRecyclerView(this.recyclerViewVideo, newConfig.orientation);
    }


    @Override
    public void displayNothingToShowHint() {
        this.scrollViewNothingToShow.setVisibility(View.VISIBLE);
    }

    private int getMovieIdFromArguments() {
        if(getArguments() != null) {
            return   getArguments().getInt(MovieDetailsFragment.ARG_SELECTED_MOVIE_ID);
        }

        return -1;
    }

    private class ClickListener implements RecyclerView.OnClickListener {
        @Override
        public void onClick(View clickedView) {
            showLoading();
            int position = recyclerViewVideo.getChildAdapterPosition(clickedView);
            movieVideoTabFragmentInteractionListener.showYouTubePlayer(
                    someMovieData.getMovieVideoResultEntities().get(position).getVideoKey(),
                    someMovieData);
        }
    }
}
