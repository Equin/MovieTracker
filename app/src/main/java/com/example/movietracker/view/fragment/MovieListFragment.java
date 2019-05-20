package com.example.movietracker.view.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie.MovieResultEntity;
import com.example.movietracker.data.entity.movie.MoviesEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.listener.OnLastElementReachedListener;
import com.example.movietracker.listener.SnapScrollListener;
import com.example.movietracker.model.model_impl.MovieModelImpl;
import com.example.movietracker.model.model_impl.UserModelImpl;
import com.example.movietracker.presenter.MovieListPresenter;
import com.example.movietracker.view.FilterAlertDialog;
import com.example.movietracker.view.MovieCardItemDecorator;
import com.example.movietracker.view.adapter.MovieListAdapter;
import com.example.movietracker.view.contract.MovieListView;
import com.example.movietracker.view.helper.RecyclerViewOrientationUtility;
import com.example.movietracker.view.helper.UtilityHelpers;
import com.example.movietracker.view.model.Filters;
import com.example.movietracker.view.model.MovieRecyclerItemPosition;
import com.example.movietracker.view.model.Option;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListFragment extends BaseFragment
        implements MovieListView,
        OnLastElementReachedListener,
        FilterAlertDialog.OnDoneButtonClickedListener,
        SearchView.OnQueryTextListener,
        MenuItem.OnActionExpandListener {

    private static final String ARG_GENRES_ENTITY = "args_genres_entity";
    private static final String ARG_SHOW_FAVORITE_MOVIES = "args_show_favorite_movies";
    private static final String TAG = MovieListFragment.class.getCanonicalName();
    private static final int RECYCLER_VIEW_CARD_ITEM_OFFSET_DPI = 8;

    public interface MovieListFragmentInteractionListener {
        void showMovieDetailScreen(int movieId);
    }

    public static MovieListFragment newInstance(GenresEntity genresEntity, boolean shouldShowFavoriteMovies) {
        MovieListFragment movieListFragment = new MovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_GENRES_ENTITY, genresEntity);
        bundle.putBoolean(ARG_SHOW_FAVORITE_MOVIES, shouldShowFavoriteMovies);
        movieListFragment.setArguments(bundle);
        return movieListFragment;
    }

    private MovieListPresenter movieListPresenter;
    private MovieListFragmentInteractionListener movieListFragmentInteractionListener;
    private MovieListAdapter movieListAdapter;
    private MovieRecyclerItemPosition movieRecyclerItemPosition;
    private MenuItem filterMenuItem;
    private MenuItem searchItem;

    @BindView(R.id.recyclerView_movies)
    RecyclerView movieRecyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.textView_nothingToShow)
    TextView textViewNothingToShow;

    public MovieListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.movieRecyclerItemPosition = new MovieRecyclerItemPosition();
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

        setupActionToolbar();

        this.movieListPresenter = new MovieListPresenter(
                this,
                new MovieModelImpl(),
                new UserModelImpl(),
                Filters.getInstance(),
                this.movieRecyclerItemPosition);

        this.movieListPresenter.initialize(shouldShowFavoriteMoviesList());
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshListener());

        RecyclerViewOrientationUtility.setLayoutManagerToRecyclerView(
                this.movieRecyclerView,
                getContext().getResources().getConfiguration().orientation);
        this.movieRecyclerView.addItemDecoration(new MovieCardItemDecorator(RECYCLER_VIEW_CARD_ITEM_OFFSET_DPI));
    }

    private void setupActionToolbar() {
        if (!shouldShowFavoriteMoviesList()) {
            setToolbarTitle(
                    UtilityHelpers.getPipeDividedGenres(
                            Filters.getInstance().getSelectedGenres()));
        } else  {
            setToolbarTitle(getString(R.string.movie_list_favorite_header));
        }

        setSupportActionBar();
        setNotTransparentToolbar();

        this.setupMenuDrawer();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MovieListFragmentInteractionListener) {
            this.movieListFragmentInteractionListener = (MovieListFragmentInteractionListener) context;
        }
    }

    /**
     * saving position and offset of currently first completely visible recycler view item
     */
    @Override
    public void onStop() {
        super.onStop();
        final LinearLayoutManager manager = (LinearLayoutManager) movieRecyclerView.getLayoutManager();
        int itemPosition = 0;
        int itemOffset = 0;

        if(manager != null) {
            itemPosition = manager.findFirstCompletelyVisibleItemPosition();
            View view = manager.findViewByPosition(itemPosition);
            itemOffset = view != null ? view.getTop() : 0;
        }

        searchItem.collapseActionView();
        this.movieListPresenter.saveRecyclerPosition(itemPosition, itemOffset);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.movieListFragmentInteractionListener = null;
        this.movieRecyclerView = null;
        this.swipeRefreshLayout = null;
        this.searchItem = null;
        this.filterMenuItem = null;
        this.textViewNothingToShow = null;
        this.movieListAdapter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.movieListPresenter.destroy();
    }

    private void setupMenuDrawer() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    /**
     * rendering movies by movieListAdapter and movieRecyclerView,
     * @param moviesEntity movies that we got from db or network
     */
    @Override
    public void renderMoviesList(MoviesEntity moviesEntity) {
        this.movieListAdapter = new MovieListAdapter(
                moviesEntity,
                new ClickListener(),
                getGenresEntity(),
                new OnFavoriteCheckedListener(),
                new OnImageViewLongClickListener());

        if (movieRecyclerView != null) {
            this.movieRecyclerView.setHasFixedSize(true);

            this.movieRecyclerView.setAdapter(movieListAdapter);
            this.movieRecyclerView.addOnScrollListener(new SnapScrollListener(this));
        }

        if (this.swipeRefreshLayout != null) {
            this.swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * changing RecyclerView layout manager on configuration change
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        RecyclerViewOrientationUtility.setLayoutManagerToRecyclerView(this.movieRecyclerView, newConfig.orientation);
    }

    /**
     * passing to movieListAdapter new moviesEntity with additional 20 movies (if repository response)
     * each time when lastElementReached triggered
     *
     * @param moviesEntity - new moviesEntity with additional movies
     */
    @Override
    public void renderAdditionalMovieListPage(MoviesEntity moviesEntity) {
        this.movieListAdapter.reloadMovieListWithAdditionalMovies(moviesEntity);
    }

    /**
     * displaying text view with text when there is nothing to show
     */
    @Override
    public void displayNothingToShowHint() {
        this.textViewNothingToShow.setVisibility(View.VISIBLE);
    }

    /**
     * initializing SearchView with ExpandListener and OnQueryTextListener
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_actions_movie_list, menu);
        filterMenuItem = menu.findItem(R.id.action_filter);

        if(shouldShowFavoriteMoviesList()) {
            filterMenuItem.setVisible(false);
        }

        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchItem.setOnActionExpandListener(this);
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;

            case R.id.action_filter:
                ClassProvider.filterAlertDialog.showFilterAlertDialog(this.getContext(), this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * filtering movies by title on text submit in searchView
     * @param query - submitted query
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        movieListAdapter.getFilter().filter(query);
        return false;
    }

    /**
     * filtering movies by title on text change in searchView
     * @param newText
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        movieListAdapter.getFilter().filter(newText);
        return false;
    }

    /**
     * hiding filterIcon from action bar on searchView expanded
     */
    @Override
    public boolean onMenuItemActionExpand(final MenuItem item) {
        filterMenuItem.setVisible(false);
        return true;
    }

    /**
     * showing filterIcon on searchView Collapsed
     */
    @Override
    public boolean onMenuItemActionCollapse(final MenuItem item) {
        filterMenuItem.setVisible(true);
        return true;
    }

    @Override
    public void showMovieDetailScreen(int movieId) {
        this.movieListFragmentInteractionListener.showMovieDetailScreen(movieId);
    }

    /**
     * Scrolling movieRecyclerView to position with offset that was saved in onStop();
     * @param itemPosition position of first completely visible item
     * @param itemOffset offset to top from item.
     */
    @Override
    public void scrollToPositionWithOffset(int itemPosition, int itemOffset) {
        this.movieRecyclerView.postOnAnimation(() -> {
            LinearLayoutManager manager =((LinearLayoutManager) movieRecyclerView.getLayoutManager());
            if (manager != null) {
                manager.scrollToPositionWithOffset(itemPosition, itemOffset);
            }

            this.movieRecyclerItemPosition.setValuesToZero();
        });
    }

    @Override
    public void lastElementReached() {
        this.movieListPresenter.lastMovieOfPageReached();
    }

    @Override
    public void onDownloadStarted() {
        showToast(R.string.download_manager_img_download_started);
    }

    @Override
    public void onDownloadCompleted() {
        showToast(R.string.download_manager_img_download_finished);
    }

    @Override
    public void onDownloadFailed() {
        showToast(R.string.download_manager_img_download_failed);
    }

    /**
     * getting movies by filters on alert dialog done button clicked
     * @param alertDialog
     */
    @Override
    public void onAlertDialogDoneButtonClicked(AlertDialog alertDialog) {
        alertDialog.dismiss();

        Option option = ClassProvider.filterAlertDialog.getFilterOptions();
        Filters.getInstance().setOrder(option.getSortOrder());
        Filters.getInstance().setSortBy(option.getSortBy().getSearchName());

        this.movieListPresenter.getMoviesByFilters(Filters.getInstance());
    }

    private GenresEntity getGenresEntity() {
        if(getArguments() != null) {
            return  (GenresEntity) getArguments().getSerializable(ARG_GENRES_ENTITY);
        }

        return null;
    }

    private boolean shouldShowFavoriteMoviesList() {
        if(getArguments() != null) {
            return   getArguments().getBoolean(ARG_SHOW_FAVORITE_MOVIES, false);
        }

        return false;
    }

    /**
     * listener for movieRecyclerView items
     * getting movieId of clicked item and passing it to movieListPresenter
     */
    private class ClickListener implements RecyclerView.OnClickListener {

        @Override
        public void onClick(View view) {
            if(view.getTag(R.id.tag_int_movie_id) != null) {
                int movieId = (int)view.getTag(R.id.tag_int_movie_id);
                MovieListFragment.this.movieListPresenter.onMovieItemClicked(movieId);
            }
        }
    }

    /**
     * making new call for movies to update list
     */
    private class SwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            Log.d(TAG, "onRefresh called from SwipeRefreshLayout");
            MovieListFragment.this.movieListPresenter.onSwipeToRefresh();
        }
    }

    /**
     * listener for movieRecyclerView favorite toggleButton
     * getting movie object from tag of clicked item and passing it to movieListPresenter
     */
    private class OnFavoriteCheckedListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView.getTag(R.id.tag_movieResultEntity_movie_object) != null) {
                MovieResultEntity movieResultEntity = (MovieResultEntity)buttonView.getTag(R.id.tag_movieResultEntity_movie_object) ;
                MovieListFragment.this.movieListPresenter.onFavoriteChecked(movieResultEntity, isChecked);
            }
        }
    }

    /**
     * listener for imageLongClick
     * getting imageName and imageSourcePath from tag of clicked item and passing it to movieListPresenter
     */
    private class OnImageViewLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            String imageName = (String) view.getTag(R.id.tag_string_image_name);
            String imageSourcePath = (String) view.getTag(R.id.tag_string_image_source_path);
            MovieListFragment.this.movieListPresenter.onImageViewLongClick(imageName,imageSourcePath);
            return true;
        }
    }
}
