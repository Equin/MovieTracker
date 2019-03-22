package com.example.movietracker.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.GenreEntity;
import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.di.components.CoreComponent;
import com.example.movietracker.presenter.MainPresenter;
import com.example.movietracker.view.contract.MainView;
import com.example.movietracker.view.custom_view.GenreView;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class MainFragment extends BaseFragment implements MainView {

    public interface MainFragmentInteractionListener {
        void showMovieListScreen(GenresEntity genreList);
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Inject
    MainPresenter mainPresenter;

    @Inject
    GenreView genreView;

    private GenresEntity genresEntity;

    private MainFragmentInteractionListener mainFragmentInteractionListener;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    public MainFragment() {
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

        this.mainPresenter.setView(this);
        this.mainPresenter.initialize();

        setSupportActionBar();
        setTransparentToolbar();
        setToolbarTitle(getString(R.string.main_fragment_toolbar_title));
        this.setupMenuDrawer();
        this.genreView.setView(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFragmentInteractionListener) {
            this.mainFragmentInteractionListener = (MainFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mainFragmentInteractionListener = null;
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
    public void renderGenreView(GenresEntity genreList) {
        this.genresEntity = genreList;
        this.genreView.renderGenreView(this.genresEntity);
    }

    @Optional
    @OnClick(R.id.main_button_search)
    public void onSearchButtonClicked(){
        this.mainPresenter.onSearchButtonClicked(this.genresEntity);
    }

    @Override
    public void openMovieListView(GenresEntity genreList) {
        this.mainFragmentInteractionListener.showMovieListScreen(genreList);
    }
}
