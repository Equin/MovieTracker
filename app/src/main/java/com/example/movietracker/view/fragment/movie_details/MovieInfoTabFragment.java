package com.example.movietracker.view.fragment.movie_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.view.fragment.BaseFragment;
import com.example.movietracker.view.helper.UtilityHelpers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieInfoTabFragment extends BaseFragment  {

    private static final String ARG_MOVIE_DETAILS = "arg_movie_details_entity";
    private  MovieDetailsEntity movieDetailsEntity;

    public static MovieInfoTabFragment newInstance(MovieDetailsEntity movieDetailsEntity) {
        MovieInfoTabFragment movieInfoTabFragment = new MovieInfoTabFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_MOVIE_DETAILS, movieDetailsEntity);
        movieInfoTabFragment.setArguments(bundle);
        return movieInfoTabFragment;
    }

    public MovieInfoTabFragment() {
        setRetainInstance(true);
    }

    @BindView(R.id.textView_tmdbRating)
    TextView textViewTmdbRating;

    @BindView(R.id.textView_imdbRating)
    TextView textViewImdbRating;

    @BindView(R.id.textView_metascoreRating)
    TextView textViewMetascoreRating;

    @BindView(R.id.textView_someelseRating)
    TextView textViewSomeElseRating;

    @BindView(R.id.textView_imdbVoteCount)
    TextView textViewImdbVoteCount;

    @BindView(R.id.textView_tmdbVoteCount)
    TextView textViewTmdbVoteCount;

    @BindView(R.id.textView_metascoreVoreCount)
    TextView textViewMetascoreVoteCount;

    @BindView(R.id.textView_somealseVoteCount)
    TextView textViewSomeElseVoteCount;

    @BindView(R.id.textView_movieStoryline)
    TextView textViewMovieStoryline;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_info_tab_item, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.movieDetailsEntity = getMovieEntityFromArguments();
        renderInfoToTab();

    }

    private void renderInfoToTab() {
            this.textViewImdbRating.setText(
                    UtilityHelpers.getAppropriateValue(this.movieDetailsEntity.getImdbVoteAverage()));

        this.textViewImdbVoteCount.setText(
                UtilityHelpers.getAppropriateValue(this.movieDetailsEntity.getImdbVoteCount()));

        this.textViewMovieStoryline.setText(
                UtilityHelpers.getAppropriateValue(this.movieDetailsEntity.getMovieOverview()));
    }

    private MovieDetailsEntity getMovieEntityFromArguments() {
        if(getArguments() != null) {
            return   (MovieDetailsEntity) getArguments().getSerializable(ARG_MOVIE_DETAILS);
        }

        return new MovieDetailsEntity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.textViewImdbRating = null;
        this.textViewImdbVoteCount = null;
        this.textViewMetascoreRating = null;
        this.textViewMetascoreVoteCount = null;
        this.textViewMovieStoryline = null;
        this.textViewTmdbRating = null;
        this.textViewSomeElseRating = null;
        this.textViewTmdbVoteCount = null;
        this.textViewSomeElseVoteCount = null;
    }
}
