package com.example.movietracker.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.presenter.MainPresenter;
import com.example.movietracker.view.contract.MainView;
import com.example.movietracker.view.custom_view.CustomGenreView;
import com.example.movietracker.view.custom_view.GenreView;

import java.util.List;

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

    private MainPresenter mainPresenter;
    private GenreView genreView;
    private GenresEntity genresEntity;

    private MainFragmentInteractionListener mainFragmentInteractionListener;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.custom_genreView)
    CustomGenreView customGenreView;

    public MainFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.mainPresenter = new MainPresenter(this);

        this.mainPresenter.getGenres();

        setSupportActionBar();
        setTransparentToolbar();
        setToolbarTitle(getString(R.string.main_fragment_toolbar_title));
        this.setupMenuDrawer();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mainPresenter.destroy();
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
        showToast(getString(resourceId));
    }

    @Override
    public void renderGenreView(GenresEntity genreList) {
        this.genresEntity = genreList;
        this.customGenreView.renderGenreView(this.genresEntity, new onCheckedListener(), 3);
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

    public class onCheckedListener implements ToggleButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            List<GenreEntity> genreEntity = genresEntity.getGenres();
            for(int i = 0; i<genreEntity.size(); i++) {
                if (genreEntity.get(i).getGenreName().equals(buttonView.getText())) {
                    genreEntity.get(i).setSelected(isChecked);
                }
            }
           // genresEntity.setGenres(genreEntity);
        }
    }
}
