package com.example.movietracker.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.data.entity.MovieListEntity;
import com.example.movietracker.di.components.CoreComponent;
import com.example.movietracker.presenter.MainPresenter;
import com.example.movietracker.presenter.MovieListPresenter;
import com.example.movietracker.view.adapter.GenreViewAdapter;
import com.example.movietracker.view.adapter.MovieListAdapter;
import com.example.movietracker.view.contract.MainView;
import com.example.movietracker.view.contract.MovieListView;
import com.example.movietracker.view.custom_view.GenreView;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListFragment extends BaseFragment implements MovieListView {

    private static final String ARG_SELECTED_GENRES = "args_selected_genres";

    public static MovieListFragment newInstance(GenresEntity genresEntity) {
        MovieListFragment movieListFragment = new MovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_SELECTED_GENRES, genresEntity);
        movieListFragment.setArguments(bundle);
        return movieListFragment;
    }

    @Inject
    MovieListPresenter movieListPresenter;

    @BindView(R.id.recyclerView_movies)
    RecyclerView movieRecyclerView;

    public MovieListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(CoreComponent.class).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.movieListPresenter.setView(this);
        this.movieListPresenter.initialize(getGenres());

        setSupportActionBar();
        this.setupMenuDrawer();
    }

    private void setupMenuDrawer() {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void showToast(int resourceId) {
        showToast("app loaded");
    }

    @Override
    public void renderMoviesList(MovieListEntity movieListEntity) {
        RecyclerView.LayoutManager rowLayoutManager = new LinearLayoutManager(
               getContext(), RecyclerView.VERTICAL, false);

        movieRecyclerView.setLayoutManager(rowLayoutManager);
        MovieListAdapter movieListAdapter = new MovieListAdapter(getContext(), movieListEntity);
        movieRecyclerView.setAdapter(movieListAdapter);
    }

    private GenresEntity getGenres() {
        if(getArguments() != null) {
            return  (GenresEntity) getArguments().getSerializable(ARG_SELECTED_GENRES);
        }

        return null;
    }
}
