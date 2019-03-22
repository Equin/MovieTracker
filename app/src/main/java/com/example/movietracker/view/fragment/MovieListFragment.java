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
import com.example.movietracker.view.contract.MainView;
import com.example.movietracker.view.contract.MovieListView;
import com.example.movietracker.view.custom_view.GenreView;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListFragment extends BaseFragment implements MovieListView {

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    @Inject
    MovieListPresenter movieListPresenter;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.movieListPresenter.setView(this);
        this.movieListPresenter.initialize();

        setSupportActionBar();
        setTransparentToolbar();
        setToolbarTitle(getString(R.string.main_fragment_toolbar_title));
        this.setupMenuDrawer();
    }

    private void setupMenuDrawer() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToast(int resourceId) {
        showToast("app loaded");
    }

    @Override
    public void renderMoviesList(MovieListEntity movieListEntity) {
        showToast(movieListEntity.getMovies().toString());
    }
}
