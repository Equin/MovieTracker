package com.example.movietracker.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.example.movietracker.R;
import com.example.movietracker.model.model_impl.UserModelImpl;
import com.example.movietracker.view.model.Filters;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.model.model_impl.GenreModelImpl;
import com.example.movietracker.presenter.MainPresenter;
import com.example.movietracker.view.contract.MainView;
import com.example.movietracker.view.custom_view.CustomGenreView;
import com.example.movietracker.view.FilterAlertDialog;
import com.google.android.material.navigation.NavigationView;

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

/**
 * Main fragment for displaying genres and menu
 */
public class MainFragment extends BaseFragment
        implements MainView,
        FilterAlertDialog.OnDoneButtonClickedListener,
        NavigationView.OnNavigationItemSelectedListener {

    /**
     * The interface Main fragment interaction listener.
     */
    public interface MainFragmentInteractionListener {
        void showMovieListScreen(GenresEntity genresEntity);
        void showFavoriteMovieListScreen(GenresEntity genresEntity);
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

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private Switch parentalControlSwitch;

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

        setupActionToolbar();

        this.mainPresenter = new MainPresenter(
                this,
                new GenreModelImpl(),
                new UserModelImpl(),
                Filters.getInstance());

        this.mainPresenter.getUser();

        this.navigationView.setNavigationItemSelectedListener(this);
        this.parentalControlSwitch = this.navigationView.getMenu()
                .findItem(R.id.parent_control).getActionView()
                .findViewById(R.id.switch_parent_control);
        this.parentalControlSwitch.setOnCheckedChangeListener(new OnMenuSwitchCheckedListener());
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

    private void setupActionToolbar() {
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
        showToast(getString(resourceId));
    }

    @Override
    public void renderGenreView(GenresEntity genreList) {
        this.customGenreView.renderGenreView(genreList,
                new OnGenreCheckedListener(), Filters.getInstance().getSelectedGenresIds());
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
    public void openFavoriteMoviesListView(GenresEntity genreList) {
        this.mainFragmentInteractionListener.showFavoriteMovieListScreen(genreList);
    }

    @Override
    public void openAlertDialog() {
        ClassProvider.filterAlertDialog.showFilterAlertDialog(this.getContext(), this);
    }

    /**
     * dismissing selections from customGenreView and filterAlertDialog
     */
    @Override
    public void dismissAllSelections() {
        this.customGenreView.dismissSelections();
        ClassProvider.filterAlertDialog.dismissSelection();
    }

    @Override
    public void openNewPasswordDialog() {
        createNewPasswordDialog(this.mainPresenter);
    }

    @Override
    public void openCheckPasswordDialog() {
        createCheckPasswordDialog(this.mainPresenter);
    }

    @Override
    public void openResetPasswordDialog() {
        createPasswordResetDialog(this.mainPresenter);
    }

    @Override
    public void dismissPasswordDialog() {
        dismissDialog();
    }

    @Override
    public void setParentalControlEnabled(boolean parentalControlEnabled) {
        if(this.parentalControlSwitch != null) {
            this.parentalControlSwitch.setChecked(parentalControlEnabled);
        }
    }

    @Override
    public void onAlertDialogDoneButtonClicked(AlertDialog alertDialog) {
        alertDialog.dismiss();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()) {
            case R.id.password_reset:
                this.mainPresenter.onPasswordResetMenuItemClicked();
                this.drawerLayout.closeDrawers();
                return false;

            case R.id.favorite:
                this.mainPresenter.onFavoriteMenuItemClicked();
                this.drawerLayout.closeDrawer(GravityCompat.START);
                return false;
        }
        return true;
    }


    /**
     * listener for customGenreView
     * getting genreId for tag of clicked item and passing it to mainPresenter.onGenreChecked
     */
    private class OnGenreCheckedListener implements ToggleButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int genreId = (int)buttonView.getTag(R.id.tag_int_genre_id);
            MainFragment.this.mainPresenter.onGenreChecked(genreId, isChecked);
        }
    }

    /**
     * listener for parental control switcher
     * passing state of switcher to MainPresenter
     */
    private class OnMenuSwitchCheckedListener implements Switch.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            MainFragment.this.mainPresenter.onParentalControlSwitchChanged(isChecked);
        }
    }
}
