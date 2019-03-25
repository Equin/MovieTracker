package com.example.movietracker.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.movietracker.R;
import com.example.movietracker.presenter.MovieDetailsPresenter;
import com.example.movietracker.view.adapter.ViewPagerAdapter;
import com.example.movietracker.view.contract.MovieDetailsView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsFragment extends BaseFragment implements MovieDetailsView {

    private static final String ARG_SELECTED_MOVIE_ID = "arg_selected_movie_id";

    public interface MovieDetailsFragmentInteractionListener {

    }

    public static MovieDetailsFragment newInstance(int movieId) {
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SELECTED_MOVIE_ID, movieId);
        movieDetailsFragment.setArguments(bundle);
        return movieDetailsFragment;
    }

    private MovieDetailsPresenter movieDetailsPresenter;
    private MainFragment.MainFragmentInteractionListener mainFragmentInteractionListener;
    private ViewPagerAdapter viewPagerAdapter;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.tabLayout_movieDetails)
    TabLayout tabLayoutMovieDetails;

    @BindView(R.id.viewPager_movieDetails)
    ViewPager viewPagerMovieDetails;

    public MovieDetailsFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.movieDetailsPresenter = new MovieDetailsPresenter();

        this.movieDetailsPresenter.setView(this);
        this.movieDetailsPresenter.initialize(getMovieIdFromArguments());
        setupTabLayout();
    }

    private void setupTabLayout() {
        this.viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        this.viewPagerMovieDetails.setAdapter(this.viewPagerAdapter);
        this.tabLayoutMovieDetails.setupWithViewPager(this.viewPagerMovieDetails);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFragment.MainFragmentInteractionListener) {
            this.mainFragmentInteractionListener = (MainFragment.MainFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mainFragmentInteractionListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.movieDetailsPresenter.destroy();
    }

    private int getMovieIdFromArguments() {
        if(getArguments() != null) {
            return   getArguments().getInt(ARG_SELECTED_MOVIE_ID);
        }

        return -1;
    }

    @Override
    public void showToast(int resourceId) {
        showToast(getString(resourceId));
    }
}
