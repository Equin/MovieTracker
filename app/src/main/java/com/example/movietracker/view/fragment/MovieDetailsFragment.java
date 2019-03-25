package com.example.movietracker.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movietracker.R;
import com.example.movietracker.presenter.MovieDetailsPresenter;
import com.example.movietracker.view.contract.MovieDetailsView;
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

    private static final String ARG_SELECTED_MOVIE_ID = "arg_selected_movie_id";

    public interface MovieDetailsFragmentInteractionListener {

    }

    public static MovieDetailsFragment newInstance(int movieId) {
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SELECTED_MOVIE_ID, movieId);
        movieDetailsFragment.setArguments(bundle);
        return movieDetailsFragment;
    }

    private MovieDetailsPresenter movieDetailsPresenter;
    private MainFragment.MainFragmentInteractionListener mainFragmentInteractionListener;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.tabLayout_movieDetails)
    TabLayout tabLayoutMovieDetails;

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

        this.movieDetailsPresenter = new MovieDetailsPresenter();

        this.movieDetailsPresenter.setView(this);
        this.movieDetailsPresenter.initialize(getMovieIdFromArguments());
        setupTabLayout();
    }

    private static String[] tabTitles = new String[]{"Info", "Cast", "Review", "Video"};

    private void setupTabLayout() {
        for (int i = 0; i<tabTitles.length; i++) {
            tabLayoutMovieDetails.addTab(tabLayoutMovieDetails.newTab().setText(tabTitles[i]), i == 0);
        }
        replaceFragment(new MovieInfoTabFragment());

      tabLayoutMovieDetails.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
          @Override
          public void onTabSelected(TabLayout.Tab tab) {
              switch (tab.getPosition()) {
                  case 0 : replaceFragment(new MovieInfoTabFragment());
                      break;
                  case 1 : replaceFragment(new MovieCastTabFragment());
                      break;
                  case 2 : replaceFragment(new MovieCastTabFragment());
                      break;
                  case 3 : replaceFragment(new MovieCastTabFragment());
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
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFragment.MainFragmentInteractionListener) {
            this.mainFragmentInteractionListener = (MainFragment.MainFragmentInteractionListener) context;
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
}
