package com.example.movietracker.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.model.model_impl.AuthModelImpl;
import com.example.movietracker.model.model_impl.GenreModelImpl;
import com.example.movietracker.model.model_impl.MovieModelImpl;
import com.example.movietracker.model.model_impl.UserModelImpl;
import com.example.movietracker.view.model.NotificationBody;
import com.example.movietracker.notification.PushNotificationSender;
import com.example.movietracker.presenter.MainPresenter;
import com.example.movietracker.service.UpdateMoviesFromServerWorker;
import com.example.movietracker.view.FilterAlertDialog;
import com.example.movietracker.view.contract.MainView;
import com.example.movietracker.view.custom_view.CustomGenreView;
import com.example.movietracker.view.custom_view.CustomToolbarSearchView;
import com.example.movietracker.view.helper.KeyboardUtility;
import com.example.movietracker.view.model.Filters;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
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
        NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = MainFragment.class.getCanonicalName();
    private static final int BACKGROUND_SYNC_REPEAT_INTERVAL_MINUTES = 60 * 12;

    /**
     * The interface Main fragment interaction listener.
     */
    public interface MainFragmentInteractionListener {
        void showMovieListScreen(GenresEntity genresEntity);
        void showMovieDetailScreen(int movieId);
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
    private Switch backgroundSyncSwitch;

    private MenuItem searchMenuItem;

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
                new AuthModelImpl(),
                Filters.getInstance());

        this.mainPresenter.createSession();

        this.navigationView.setNavigationItemSelectedListener(this);
        this.parentalControlSwitch = this.navigationView.getMenu()
                .findItem(R.id.parent_control).getActionView()
                .findViewById(R.id.menu_switcher);
        this.backgroundSyncSwitch = this.navigationView.getMenu()
                .findItem(R.id.background_sync).getActionView()
                .findViewById(R.id.menu_switcher);

        this.parentalControlSwitch.setOnCheckedChangeListener(new OnMenuSwitchCheckedListener());
        this.backgroundSyncSwitch.setOnCheckedChangeListener(new OnMenuBackgroundSyncSwitchCheckedListener ());
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
        this.parentalControlSwitch = null;
        this.backgroundSyncSwitch = null;
        this.customGenreView = null;
        this.drawerLayout = null;
        this.navigationView = null;
        this.searchMenuItem = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        KeyboardUtility.hideKeyboard(getContext(), getView());
        dismissDialog();
        this.searchMenuItem.collapseActionView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mainPresenter.destroy();
    }

    private void setupActionToolbar() {
        setToolbarTitle(getString(R.string.main_fragment_toolbar_title));
        setTransparentToolbar();
        setSupportActionBar();
        this.setupMenuDrawer();
    }

    private void setupMenuDrawer() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    /**
     * initialize searchMenuItem and customSearchView providing it with SearchFeatureRecyclerItemClickListener
     * and MovieModelImpl
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_actions_main_screen, menu);

        this.searchMenuItem = menu.findItem(R.id.action_search);
        ClassProvider.searchFeature.init((CustomToolbarSearchView) this.searchMenuItem.getActionView(), new MovieModelImpl(), new SearchFeatureRecyclerItemClickListener());
        super.onCreateOptionsMenu(menu, inflater);
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

    /**
     * rendering genres to CustomGenreView
     * @param genreList
     */
    @Override
    public void renderGenreView(GenresEntity genreList) {
        this.customGenreView.renderGenreView(genreList,
                new OnGenreCheckedListener(), Filters.getInstance().getSelectedGenresIds());
    }

    @Optional
    @OnClick(R.id.main_button_search)
    public void onSearchButtonClicked() {
        this.mainPresenter.onSearchButtonClicked(
                ClassProvider.filterAlertDialog.getFilterOptions()
        );
    }

    @Optional
    @OnClick(R.id.main_button_filter)
    public void onFilterButtonClicked() {
        this.mainPresenter.onFilterButtonClicked();
    /*    PushNotificationSender sender = new PushNotificationSender(this.getContext());
        sender.sendNotification(new NotificationBody("Hello", "title"), 0);*/
    }

    @Optional
    @OnClick(R.id.main_button_cancel)
    public void onCancelButtonClicked() {
        this.mainPresenter.onCancelButtonClicked();
    }

    @Override
    public void openMovieListView(GenresEntity genresEntity) {
        this.mainFragmentInteractionListener.showMovieListScreen(genresEntity);
    }

    @Override
    public void openMovieDetailScreen(int movieId) {
        this.mainFragmentInteractionListener.showMovieDetailScreen(movieId);
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
    public void openLoginDialog() {
        createLoginDialog(this.mainPresenter);
    }

    @Override
    public void setParentalControlEnabled(boolean parentalControlEnabled) {
        if (this.parentalControlSwitch != null) {
            this.parentalControlSwitch.setChecked(parentalControlEnabled);
        }
    }

    @Override
    public void setBackgroundSyncEnabled(boolean backgroundSyncEnabled) {
        if (this.backgroundSyncSwitch != null) {
            this.backgroundSyncSwitch.setChecked(backgroundSyncEnabled);
        }
    }

    @Override
    public void showLoginMenuItem() {
        navigationView.getMenu().findItem(R.id.log_in).setVisible(true);
    }

    @Override
    public void hideLoginMenuItem() {
        navigationView.getMenu().findItem(R.id.log_in).setVisible(false);
    }

    @Override
    public void showLogoutMenuItem() {
        navigationView.getMenu().findItem(R.id.log_out).setVisible(true);
    }

    @Override
    public void hideLogoutMenuItem() {
        navigationView.getMenu().findItem(R.id.log_out).setVisible(false);
    }

    @Override
    public void setUsernameToHeaderView(String tmdbUsername) {
        TextView username = navigationView.getHeaderView(0).findViewById(R.id.textView_user_name);
        username.setText(tmdbUsername);
    }

    /**
     * canceling background syncing
     */
    @Override
    public void stopBackgroundSync() {
        WorkManager.getInstance().cancelUniqueWork(TAG);
    }

    /**
     * starting background sync of movies every 4h
     */
    @Override
    public void startBackgroundSync() {
        Constraints.Builder constraintsBuilder = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.NOT_ROAMING);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            constraintsBuilder.setRequiresDeviceIdle(true);
        }

        PeriodicWorkRequest backgroundSyncWorkRequest = new PeriodicWorkRequest.Builder(
                UpdateMoviesFromServerWorker.class,
                BACKGROUND_SYNC_REPEAT_INTERVAL_MINUTES + 2,
                TimeUnit.MINUTES,
                BACKGROUND_SYNC_REPEAT_INTERVAL_MINUTES,
                TimeUnit.MINUTES)
                .setConstraints(constraintsBuilder.build())
                .build();

        WorkManager.getInstance().enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, backgroundSyncWorkRequest);
    }

    /**
     * dismissing alert dialog on Done button clicked
     * @param alertDialog
     */
    @Override
    public void onAlertDialogDoneButtonClicked(AlertDialog alertDialog) {
        alertDialog.dismiss();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.password_reset:
                this.mainPresenter.onPasswordResetMenuItemClicked();
                this.drawerLayout.closeDrawers();
                return false;

            case R.id.favorite:
                this.mainPresenter.onFavoriteMenuItemClicked();
                this.drawerLayout.closeDrawer(GravityCompat.START);
                return false;

            case R.id.log_in:
                this.drawerLayout.closeDrawer(GravityCompat.START);
                this.mainPresenter.onLoginMenuItemClicked();
                return false;

            case R.id.log_out:
                this.drawerLayout.closeDrawer(GravityCompat.START);
                this.mainPresenter.onLogoutMenuItemClicked();
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
            int genreId = (int) buttonView.getTag(R.id.tag_int_genre_id);
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

    /**
     * listener for background sync switcher
     * passing state of switcher to MainPresenter
     */
    private class OnMenuBackgroundSyncSwitchCheckedListener implements Switch.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            MainFragment.this.mainPresenter.onBackgroundSyncSwitchChanged(isChecked);
        }
    }

    /**
     * listener for movieRecyclerView items
     * getting movieId of clicked item and passing it to movieListPresenter
     */
    private class SearchFeatureRecyclerItemClickListener implements RecyclerView.OnClickListener {

        @Override
        public void onClick(View view) {
            if(view.getTag(R.id.tag_int_movie_id) != null) {
                int movieId = (int)view.getTag(R.id.tag_int_movie_id);
                MainFragment.this.mainPresenter.onMovieItemClicked(movieId);
            }
        }
    }
}
