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
import com.example.movietracker.data.entity.Filters;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.model.model_impl.GenreModelImpl;
import com.example.movietracker.presenter.MainPresenter;
import com.example.movietracker.view.contract.MainView;
import com.example.movietracker.view.custom_view.CustomGenreView;
import com.example.movietracker.view.FilterAlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class MainFragment extends BaseFragment implements MainView, FilterAlertDialog.OnDoneButtonClickedListener {

    public interface MainFragmentInteractionListener {
        void showMovieListScreen(GenresEntity genresEntity);
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private MainPresenter mainPresenter;
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

        setSupportActionBar();
        setTransparentToolbar();
        setToolbarTitle(getString(R.string.main_fragment_toolbar_title));
        this.setupMenuDrawer();

        this.getGenres();
    }

    private void getGenres() {
        if(this.mainPresenter == null) {
            this.mainPresenter = new MainPresenter(
                    this,
                    new GenreModelImpl(),
                    Filters.getInstance());

            this.mainPresenter.getGenres();
        } else {
            this.mainPresenter.getLocalGenres();
        }
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
        this.customGenreView.renderGenreView(genreList,
                new onCheckedListener());
    }

    @Optional
    @OnClick(R.id.main_button_search)
    public void onSearchButtonClicked(){
         this.mainPresenter.onSearchButtonClicked(
                 ClassProvider.filterAlertDialog.getFilterOptions()
         );
    }

    @Optional
    @OnClick(R.id.main_button_filter)
    public void onFilterButtonClicked(){
        this.mainPresenter.onFilterButtonClicked();
    }

    @Optional
    @OnClick(R.id.main_button_cancel)
    public void onCancelButtonClicked(){
        this.mainPresenter.onCancelButtonClicked();
    }

    @Override
    public void openMovieListView(GenresEntity genresEntity) {
        this.mainFragmentInteractionListener.showMovieListScreen(genresEntity);
    }

    @Override
    public void openAlertDialog() {
        ClassProvider.filterAlertDialog.showFilterAlertDialog(this.getContext(), this);
    }

    @Override
    public void dismissAllSelections() {
        this.customGenreView.dismisSelections();
        ClassProvider.filterAlertDialog.dismissSelection();
    }

    @Override
    public void OnAlertDialogDoneButtonClicked(AlertDialog alertDialog) {
            alertDialog.dismiss();
    }

    public class onCheckedListener implements ToggleButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            MainFragment.this.mainPresenter.onGenreChecked(buttonView.getText().toString(), isChecked);
        }
    }
}
