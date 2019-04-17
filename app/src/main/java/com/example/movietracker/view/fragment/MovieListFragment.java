package com.example.movietracker.view.fragment;

import android.content.Context;
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
import com.example.movietracker.data.entity.MoviesEntity;
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
import com.example.movietracker.view.helper.UtilityHelpers;
import com.example.movietracker.view.model.Filters;
import com.example.movietracker.view.model.MovieRecyclerItemPosition;
import com.example.movietracker.view.model.Option;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListFragment extends BaseFragment
        implements MovieListView,
        OnLastElementReachedListener,
        FilterAlertDialog.OnDoneButtonClickedListener {

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

        this.movieRecyclerView.addItemDecoration(new MovieCardItemDecorator(RECYCLER_VIEW_CARD_ITEM_OFFSET_DPI));
    }

    private void setupActionToolbar() {
        setSupportActionBar();
        setNotTransparentToolbar();

        if (!shouldShowFavoriteMoviesList()) {
            setToolbarTitle(
                    UtilityHelpers.getPipeDividedGenres(
                            Filters.getInstance().getSelectedGenres()));
        } else  {
            setToolbarTitle(getString(R.string.movie_list_favorite_header));
        }

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

        this.movieListPresenter.saveRecyclerPosition(itemPosition, itemOffset);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.movieListFragmentInteractionListener = null;
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
                new OnFavoriteCheckedListener());

        LinearLayoutManager rowLayoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);
        this.movieRecyclerView.setLayoutManager(rowLayoutManager);
        this.movieRecyclerView.setHasFixedSize(true);

        this.movieRecyclerView.setAdapter(movieListAdapter);
        this.movieRecyclerView.addOnScrollListener(new SnapScrollListener(this));
        this.swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * passing to movieListAdapter new moviesEntity with additional 20 movies (if repository response)
     * each time when lastElementReached triggered
     *
     * @param moviesEntity - new moviesEntity with additional movies
     */
    @Override
    public void renderAdditionalMovieListPage(MoviesEntity moviesEntity) {
        this.movieListAdapter.reloadeMovieListWithAdditionalMovies(moviesEntity);
    }

    /**
     * displaying text view with text when there is nothing to show
     */
    @Override
    public void displayNothingToShowHint() {
        this.textViewNothingToShow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_actions, menu);

        if(shouldShowFavoriteMoviesList()) {
           MenuItem filterMenuItem = menu.findItem(R.id.action_filter);
            filterMenuItem.setVisible(false);
        }

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
     * getting position of clicked item and passing it to movieListPresenter
     */
    private class ClickListener implements RecyclerView.OnClickListener {

        @Override
        public void onClick(View v) {
            int itemPosition = MovieListFragment.this.movieRecyclerView.getChildAdapterPosition(v);
            MovieListFragment.this.movieListPresenter.onMovieItemClicked(itemPosition);
        }
    }

    private class SwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            Log.d(TAG, "onRefresh called from SwipeRefreshLayout");
            MovieListFragment.this.movieListPresenter.onSwipeToRefresh();
        }
    }

    /**
     * listener for movieRecyclerView favorite toggleButton
     * getting position from tag of clicked item and passing it to movieListPresenter
     */
    private class OnFavoriteCheckedListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView.getTag(R.id.tag_int_movie_item_position) != null) {
                int position = (int)buttonView.getTag(R.id.tag_int_movie_item_position) ;
                MovieListFragment.this.movieListPresenter.onFavoriteChecked(position, isChecked);
            }
        }
    }
}
