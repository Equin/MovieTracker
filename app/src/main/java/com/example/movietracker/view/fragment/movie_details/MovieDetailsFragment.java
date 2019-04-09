package com.example.movietracker.view.fragment.movie_details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.net.constant.NetConstant;
import com.example.movietracker.presenter.MovieDetailsPresenter;
import com.example.movietracker.view.contract.MovieDetailsView;
import com.example.movietracker.view.fragment.BaseFragment;
import com.example.movietracker.view.fragment.MainFragment;
import com.example.movietracker.view.helper.UtilityHelpers;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsFragment extends BaseFragment implements MovieDetailsView {

    public static final String ARG_SELECTED_MOVIE_ID = "arg_selected_movie_id";

    private static String[] tabTitles = new String[]{"Info", "Cast", "Review", "Video"};

    public interface MovieDetailsFragmentInteractionListener {
        void openNewFragmentInTab(Fragment fragment);
    }

    public static MovieDetailsFragment newInstance(int movieId) {
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SELECTED_MOVIE_ID, movieId);
        movieDetailsFragment.setArguments(bundle);
        return movieDetailsFragment;
    }

    private MovieDetailsPresenter movieDetailsPresenter;
    private MovieDetailsFragmentInteractionListener movieDetailsFragmentInteractionListener;
    private MovieDetailsEntity movieDetailsEntity;
    private int movieId;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.tabLayout_movieDetails)
    TabLayout tabLayoutMovieDetails;

    @BindView(R.id.imageView_moviePoster_details)
    ImageView imageViewMoviePoster;

    @BindView(R.id.textView_movieReleaseDate_details)
    TextView textViewMovieReleaseDate;

    @BindView(R.id.textView_movieDuration_details)
    TextView textViewMovieDuration;

    @BindView(R.id.textView_movieTitle_details)
    TextView textViewMovieTitle;

    @BindView(R.id.textView_MovieGenres_details)
    TextView textViewMovieGenres;

    public MovieDetailsFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.movieId = getMovieIdFromArguments();

        this.movieDetailsPresenter = new MovieDetailsPresenter(this);
        this.movieDetailsPresenter.getMovieDetails(this.movieId);
    }

    private void setupTabLayout() {
        for (int i = 0; i<tabTitles.length; i++) {
            tabLayoutMovieDetails.addTab(tabLayoutMovieDetails.newTab().setText(tabTitles[i]), i == 0);
        }

        replaceFragment(MovieInfoTabFragment.newInstance(movieDetailsEntity));

      tabLayoutMovieDetails.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
          @Override
          public void onTabSelected(TabLayout.Tab tab) {
              switch (tab.getPosition()) {
                  case 0 : replaceFragment(MovieInfoTabFragment.newInstance(movieDetailsEntity));
                      break;
                  case 1 : replaceFragment(MovieCastTabFragment.newInstance(movieId));
                      break;
                  case 2 : replaceFragment(MovieReviewTabFragment.newInstance(movieId));
                      break;
                  case 3 : replaceFragment(MovieVideoTabFragment.newInstance(movieId));
                      break;
                  default: replaceFragment(new MovieCastTabFragment());
              }
          }

          @Override
          public void onTabUnselected(TabLayout.Tab tab) {

          }

          @Override
          public void onTabReselected(TabLayout.Tab tab) {

          }
      });
    }

    private void replaceFragment(Fragment fragment) {
        this.movieDetailsFragmentInteractionListener.openNewFragmentInTab(fragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MovieDetailsFragmentInteractionListener) {
            this.movieDetailsFragmentInteractionListener = (
                    MovieDetailsFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.movieDetailsFragmentInteractionListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.movieDetailsPresenter.destroy();
    }

    private int getMovieIdFromArguments() {
        if(getArguments() != null) {
            return   getArguments().getInt(ARG_SELECTED_MOVIE_ID);
        }

        return -1;
    }

    @Override
    public void showToast(int resourceId) {
        showToast(getString(resourceId));
    }

    @Override
    public void renderMovieDetails(MovieDetailsEntity movieDetailsEntity) {
        this.movieDetailsEntity = movieDetailsEntity;
        setupTabLayout();
        renderMovieDetailView();
    }

    private void renderMovieDetailView() {
        this.textViewMovieDuration.setText(
                UtilityHelpers.getAppropriateValue(this.movieDetailsEntity.getMovieRuntime()) + " min");
        this.textViewMovieTitle.setText(
                this.movieDetailsEntity.getMovieTitle());
        this.textViewMovieReleaseDate.setText(
                UtilityHelpers.getYear(this.movieDetailsEntity.getMovieReleaseDate()));
        this.textViewMovieGenres.setText(
                UtilityHelpers.getPipeDividedGenres(this.movieDetailsEntity.getGenres()));

        Glide
                .with(this)
                .load(NetConstant.IMAGE_BASE_URL + movieDetailsEntity.getMoviePosterPath())
                .centerCrop()
                .into(this.imageViewMoviePoster);
    }
}
