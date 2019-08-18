package com.example.movietracker.view.fragment.movie_details;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.presenter.MovieDetailsTabLayoutPresenter;
import com.example.movietracker.view.item_decorators.MovieCardItemDecorator;
import com.example.movietracker.view.adapter.CastListAdapter;
import com.example.movietracker.view.contract.TabLayoutView;
import com.example.movietracker.view.fragment.BaseFragment;
import com.example.movietracker.listener.SnapScrollListener;
import com.example.movietracker.view.helper.RecyclerViewOrientationUtility;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieCastTabFragment extends BaseFragment implements TabLayoutView<MovieCastsEntity> {

    private static final int RECYCLER_VIEW_CARD_ITEM_OFFSET_DPI = 4;

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

    @BindView(R.id.scrollView_nothingToShow)
    NestedScrollView scrollViewNothingToShow;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_cast_tab_item, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    /**
     * initializing MovieDetailsTabLayoutPresenter and getting MovieCasts
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerViewOrientationUtility.setLayoutManagerToRecyclerView(this.recyclerViewCastList,
                getContext().getResources().getConfiguration().orientation);

        this.movieDetailsTabLayoutPresenter = new MovieDetailsTabLayoutPresenter(this);
        this.movieDetailsTabLayoutPresenter.getMovieCasts(getMovieIdFromArguments());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.recyclerViewCastList = null;
        this.scrollViewNothingToShow = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.movieDetailsTabLayoutPresenter != null) {
            this.movieDetailsTabLayoutPresenter.destroy();
        }
    }

    /**
     * changing recyclerViewCastList orientation according to configuration
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        RecyclerViewOrientationUtility.setLayoutManagerToRecyclerView(this.recyclerViewCastList, newConfig.orientation);
    }

    /**
     * setting castListAdapter with MovieCastsEntity to recyclerViewCastList adding MovieCardItemDecorator and SnapScrollListener
     * @param someMovieData
     */
    @Override
    public void renderInfoToTab(MovieCastsEntity someMovieData) {
        CastListAdapter castListAdapter = new CastListAdapter(someMovieData, new OnImageViewLongClickListener());
        this.recyclerViewCastList.setAdapter(castListAdapter);

        this.recyclerViewCastList.addItemDecoration(new MovieCardItemDecorator(RECYCLER_VIEW_CARD_ITEM_OFFSET_DPI));
        this.recyclerViewCastList.addOnScrollListener(new SnapScrollListener(this));
    }

    @Override
    public void displayNothingToShowHint() {
        this.scrollViewNothingToShow.setVisibility(View.VISIBLE);
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

    private int getMovieIdFromArguments() {
        if(getArguments() != null) {
            return   getArguments().getInt(MovieDetailsFragment.ARG_SELECTED_MOVIE_ID);
        }

        return -1;
    }

    /**
     * listener for imageLongClick
     * getting imageName and imageSourcePath from tag of clicked item and passing it to movieDetailsTabLayoutPresenter
     */
    private class OnImageViewLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            String imageName = (String) view.getTag(R.id.tag_string_image_name);
            String imageSourcePath = (String) view.getTag(R.id.tag_string_image_source_path);
            MovieCastTabFragment.this.movieDetailsTabLayoutPresenter.onImageViewLongClick(imageName,imageSourcePath);
            return true;
        }
    }
}
