package com.example.movietracker.view.fragment.movie_details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.presenter.MovieDetailsTabLayoutPresenter;
import com.example.movietracker.view.adapter.VideoListAdapter;
import com.example.movietracker.view.contract.TabLayoutView;
import com.example.movietracker.view.fragment.BaseFragment;
import com.example.movietracker.listener.SnapScrollListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieVideoTabFragment<V extends MovieVideosEntity> extends BaseFragment implements TabLayoutView<MovieVideosEntity> {

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

    @BindView(R.id.textView_nothingToShow)
    TextView textViewNothingToShow;

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
        this.movieDetailsTabLayoutPresenter.destroy();
    }

    private MovieVideosEntity someMovieData;

    @Override
    public void renderInfoToTab(MovieVideosEntity someMovieData) {
        this.someMovieData = someMovieData;
        RecyclerView.LayoutManager rowLayoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);

        this.recyclerViewVideo.setLayoutManager(rowLayoutManager);
        VideoListAdapter reviewListAdapter = new VideoListAdapter(someMovieData, new ClickListener());
        this.recyclerViewVideo.setAdapter(reviewListAdapter);

        this.recyclerViewVideo.addOnScrollListener(new SnapScrollListener(this));
    }

    @Override
    public void displayNothingToShowHint() {
        this.textViewNothingToShow.setVisibility(View.VISIBLE);
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
