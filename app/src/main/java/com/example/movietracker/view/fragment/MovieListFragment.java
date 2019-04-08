package com.example.movietracker.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieRequestEntity;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.di.DataProvider;
import com.example.movietracker.listener.OnLastElementReachedListener;
import com.example.movietracker.presenter.MovieListPresenter;
import com.example.movietracker.view.adapter.MovieListAdapter;
import com.example.movietracker.view.contract.MovieListView;
import com.example.movietracker.listener.SnapScrollListener;
import com.example.movietracker.view.FilterAlertDialog;
import com.example.movietracker.view.model.Option;
import com.example.movietracker.view.helper.UtilityHelpers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListFragment extends BaseFragment
        implements MovieListView,
                   OnLastElementReachedListener,
                   FilterAlertDialog.OnDoneButtonClickedListener {

    private static final String ARG_MOVIE_REQUEST_ENTITY = "args_movie_request_entity";

    public interface MovieListFragmentInteractionListener {
        void showMovieDetailScreen(int movieId);
    }

    public static MovieListFragment newInstance(MovieRequestEntity movieRequestEntity) {
        MovieListFragment movieListFragment = new MovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_MOVIE_REQUEST_ENTITY, movieRequestEntity);
        movieListFragment.setArguments(bundle);
        return movieListFragment;
    }

    private MovieListPresenter movieListPresenter;
    private MovieListFragmentInteractionListener movieListFragmentInteractionListener;
    private MovieListAdapter movieListAdapter;

    private boolean isActionAllowed = true;
    private int totalPages;

    @BindView(R.id.recyclerView_movies)
    RecyclerView movieRecyclerView;

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
                        DataProvider.movieRequestEntity.getSelectedGenres().getGenres()));
        this.setupMenuDrawer();

        getMovies();
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
        this.totalPages = moviesEntity.getTotalPages();

        RecyclerView.LayoutManager rowLayoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);

        this.movieRecyclerView.setLayoutManager(rowLayoutManager);
        this.movieListAdapter = new MovieListAdapter(
                moviesEntity,
                new ClickListener(
                        moviesEntity,
                        movieRecyclerView,
                        movieListPresenter),
                DataProvider.movieRequestEntity.getGenresEntity());

        this.movieRecyclerView.setAdapter(movieListAdapter);
        this.movieRecyclerView.addOnScrollListener(new SnapScrollListener(this));
        this.isActionAllowed = true;
    }

    @Override
    public void renderAdditionalMovieListPage(MoviesEntity moviesEntity) {
        this.movieListAdapter.updateMovieList(moviesEntity);
        this.isActionAllowed = true;
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
    public void lastElementReached() {
        if(this.isActionAllowed) {
            this.isActionAllowed = false;
            if (this.totalPages > DataProvider.movieRequestEntity.getPage()) {
                DataProvider.movieRequestEntity.incrementPage();
                showToast(DataProvider.movieRequestEntity.getPage() + "page");
                this.movieListPresenter.getMoviesWithPagination(DataProvider.movieRequestEntity);
            } else {
                showToast(R.string.movie_list_there_are_no_pages);
                this.isActionAllowed = true;
            }
        }
    }

    @Override
    public void OnAlertDialogDoneButtonClicked(AlertDialog alertDialog) {
            alertDialog.dismiss();

            Option option = ClassProvider.filterAlertDialog.getFilterOptions();
            DataProvider.movieRequestEntity.setOrder(option.getSortOrder());
            DataProvider.movieRequestEntity.setSortBy(option.getSortBy().getSearchName());

        this.movieListPresenter.getMoviesByFilters(DataProvider.movieRequestEntity);
    }

    private void getMovies() {
        if(this.movieListPresenter != null) {
            renderMoviesList(this.movieListPresenter.getMoviesEntity());
        } else {
            this.movieListPresenter = new MovieListPresenter(this);
            this.movieListPresenter.getMoviesByFilters(DataProvider.movieRequestEntity);
        }
    }

    private MovieRequestEntity getMovieRequestEntity() {
        if(getArguments() != null) {
            return  (MovieRequestEntity) getArguments().getSerializable(ARG_MOVIE_REQUEST_ENTITY);
        }

        return null;
    }

    private class ClickListener implements RecyclerView.OnClickListener {
        MoviesEntity moviesEntity;
        RecyclerView recyclerView;
        MovieListPresenter movieListPresenter;

        public ClickListener(MoviesEntity moviesEntity, RecyclerView recyclerView, MovieListPresenter movieListPresenter) {
            this.moviesEntity = moviesEntity;
            this.recyclerView = recyclerView;
            this.movieListPresenter = movieListPresenter;
        }

        @Override
        public void onClick(View v) {
            int itemPosition = this.recyclerView.getChildAdapterPosition(v);

            this.movieListPresenter.onMovieItemClicked(
                    this.moviesEntity.getMovies().get(itemPosition).getMovieId());
        }
    }
}
