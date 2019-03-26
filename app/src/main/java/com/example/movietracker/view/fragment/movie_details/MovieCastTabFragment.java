package com.example.movietracker.view.fragment.movie_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieCastsEntity;
import com.example.movietracker.presenter.MovieDetailsTabLayoutPresenter;
import com.example.movietracker.view.adapter.CastListAdapter;
import com.example.movietracker.view.contract.TabLayoutView;
import com.example.movietracker.view.fragment.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieCastTabFragment extends BaseFragment implements TabLayoutView<MovieCastsEntity> {

    public static MovieCastTabFragment newInstance(int movieId) {
        MovieCastTabFragment movieCastTabFragment = new MovieCastTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MovieDetailsFragment.ARG_SELECTED_MOVIE_ID, movieId);
        movieCastTabFragment.setArguments(bundle);
        return movieCastTabFragment;
    }

    public MovieCastTabFragment() {
        setRetainInstance(true);
    }

    private MovieDetailsTabLayoutPresenter movieDetailsTabLayoutPresenter;


    @BindView(R.id.recyclerView_castList)
    RecyclerView recyclerViewCastList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_cast_tab_item, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.movieDetailsTabLayoutPresenter = new MovieDetailsTabLayoutPresenter();

        this.movieDetailsTabLayoutPresenter.setView(this);
        this.movieDetailsTabLayoutPresenter.initialize();
        this.movieDetailsTabLayoutPresenter.getMovieCasts(getMovieIdFromArguments());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.movieDetailsTabLayoutPresenter.destroy();
    }

    @Override
    public void showToast(int resourceId) {

    }

    @Override
    public void renderInfoToTab(MovieCastsEntity someMovieData) {
        RecyclerView.LayoutManager rowLayoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);
        recyclerViewCastList.setLayoutManager(rowLayoutManager);
        CastListAdapter castListAdapter = new CastListAdapter(getContext(), someMovieData);
        recyclerViewCastList.setAdapter(castListAdapter);
    }

    private int getMovieIdFromArguments() {
        if(getArguments() != null) {
            return   getArguments().getInt(MovieDetailsFragment.ARG_SELECTED_MOVIE_ID);
        }

        return -1;
    }
}
