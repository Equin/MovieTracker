package com.example.movietracker.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.movietracker.R;
import com.example.movietracker.model.model_impl.UserModelImpl;
import com.example.movietracker.view.model.Filters;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.listener.OnLastElementReachedListener;
import com.example.movietracker.model.model_impl.MovieModelImpl;
import com.example.movietracker.presenter.MovieListPresenter;
import com.example.movietracker.view.adapter.MovieListAdapter;
import com.example.movietracker.view.contract.MovieListView;
import com.example.movietracker.listener.SnapScrollListener;
import com.example.movietracker.view.FilterAlertDialog;
import com.example.movietracker.view.model.MovieRecyclerItemPosition;
import com.example.movietracker.view.model.Option;
import com.example.movietracker.view.helper.UtilityHelpers;

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
    private static final String TAG = MovieListFragment.class.getCanonicalName();

    public interface MovieListFragmentInteractionListener {
        void showMovieDetailScreen(int movieId);
    }

    public static MovieListFragment newInstance(GenresEntity genresEntity) {
        MovieListFragment movieListFragment = new MovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_GENRES_ENTITY, genresEntity);
        movieListFragment.setArguments(bundle);
        return movieListFragment;
    }

    private MovieListPresenter movieListPresenter;
    private MovieListFragmentInteractionListener movieListFragmentInteractionListener;
    private MovieListAdapter movieListAdapter;

    @BindView(R.id.recyclerView_movies)
    RecyclerView movieRecyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    public MovieListFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setSupportActionBar();
        setNotTransparentToolbar();
        setToolbarTitle(
                UtilityHelpers.getPipeDividedGenres(
                        Filters.getInstance().getSelectedGenres()));
        this.setupMenuDrawer();

        this.movieListPresenter = new MovieListPresenter(
                this,
                new MovieModelImpl(),
                new UserModelImpl(),
                MovieRecyclerItemPosition.getInstance());

        this.movieListPresenter.initialize(Filters.getInstance());

        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshListener());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MovieListFragmentInteractionListener) {
            this.movieListFragmentInteractionListener = (MovieListFragmentInteractionListener) context;
        }
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

    @Override
    public void renderMoviesList(MoviesEntity moviesEntity) {

        RecyclerView.LayoutManager rowLayoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);

        this.movieRecyclerView.setLayoutManager(rowLayoutManager);
        this.movieListAdapter = new MovieListAdapter(
                moviesEntity,
                new ClickListener(),
                getGenresEntity(),
                new OnFavoriteCheckedListener());

        this.movieRecyclerView.setAdapter(movieListAdapter);
        this.movieRecyclerView.addOnScrollListener(new SnapScrollListener(this));
        this.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void renderAdditionalMovieListPage(MoviesEntity moviesEntity) {
        this.movieListAdapter.updateMovieList(moviesEntity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_actions, menu);
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

    @Override
    public void scrollToMovie(int itemPosition, int itemOffset) {
        this.movieRecyclerView.postOnAnimation(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager manager =((LinearLayoutManager) movieRecyclerView.getLayoutManager());
                if (manager != null) {
                    manager.scrollToPositionWithOffset(itemPosition, itemOffset);
                }

                MovieRecyclerItemPosition.getInstance().setValuesToZero();
            }
        });
    }

    @Override
    public void lastElementReached() {
        this.movieListPresenter.lastMovieOfPageReached();
    }

    @Override
    public void OnAlertDialogDoneButtonClicked(AlertDialog alertDialog) {
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

    private class ClickListener implements RecyclerView.OnClickListener {

        @Override
        public void onClick(View v) {
            int itemPosition = MovieListFragment.this.movieRecyclerView.getChildAdapterPosition(v);
            int itemOffset = v.getTop();
            MovieListFragment.this.movieListPresenter.onMovieItemClicked(itemPosition, itemOffset);
        }
    }

    private class SwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            Log.d(TAG, "onRefresh called from SwipeRefreshLayout");
            Filters.getInstance().setPage(1);
            MovieListFragment.this.movieListPresenter.getMoviesByFilters(Filters.getInstance());
        }
    }

    private class OnFavoriteCheckedListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = movieRecyclerView.getChildLayoutPosition((View) buttonView.getParent().getParent());
            MovieListFragment.this.movieListPresenter.onFavoriteChecked(position, isChecked);
        }
    }
}
