package com.example.movietracker.view.fragment.movie_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.presenter.MovieDetailsTabLayoutPresenter;
import com.example.movietracker.view.adapter.ReviewListAdapter;
import com.example.movietracker.view.contract.TabLayoutView;
import com.example.movietracker.view.fragment.BaseFragment;
import com.example.movietracker.listener.SnapScrollListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewTabFragment extends BaseFragment implements TabLayoutView<MovieReviewsEntity> {

    public static MovieReviewTabFragment newInstance(int movieId) {
        MovieReviewTabFragment movieReviewTabFragment = new MovieReviewTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MovieDetailsFragment.ARG_SELECTED_MOVIE_ID, movieId);
        movieReviewTabFragment.setArguments(bundle);
        return movieReviewTabFragment;
    }

    public MovieReviewTabFragment() {
        setRetainInstance(true);
    }

    private MovieDetailsTabLayoutPresenter movieDetailsTabLayoutPresenter;

    @BindView(R.id.recyclerView_reviewList)
    RecyclerView recyclerViewReview;

    @BindView(R.id.scrollView_nothingToShow)
    NestedScrollView scrollViewNothingToShow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_review_tab_item, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    /**
     * initializing MovieDetailsTabLayoutPresenter and getting movie reviews
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.movieDetailsTabLayoutPresenter = new MovieDetailsTabLayoutPresenter(this);
        this.movieDetailsTabLayoutPresenter.getMovieReviews(getMovieIdFromArguments());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.scrollViewNothingToShow = null;
        this.recyclerViewReview = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(this.movieDetailsTabLayoutPresenter != null) {
            this.movieDetailsTabLayoutPresenter.destroy();
        }
    }

    /**
     * rendering movieReviewEntity to recyclerViewList with ReviewListAdapter and SnapScrollListener
     * @param someMovieData
     */
    @Override
    public void renderInfoToTab(MovieReviewsEntity someMovieData) {
        RecyclerView.LayoutManager rowLayoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);
        this.recyclerViewReview.setLayoutManager(rowLayoutManager);
        this.recyclerViewReview.setAdapter(new ReviewListAdapter(someMovieData));
        this.recyclerViewReview.addOnScrollListener(new SnapScrollListener(this));
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

    @Override
    public void onDownloadStarted() {
        // left empty
    }

    @Override
    public void onDownloadCompleted() {
        // left empty
    }

    @Override
    public void onDownloadFailed() {
        // left empty
    }
}
