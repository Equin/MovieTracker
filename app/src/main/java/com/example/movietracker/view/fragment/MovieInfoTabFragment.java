package com.example.movietracker.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieDetailsEntity;
import com.example.movietracker.view.contract.TabLayoutView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;

public class MovieInfoTabFragment extends BaseFragment implements TabLayoutView<MovieDetailsEntity> {


    public MovieInfoTabFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_info_tab_item, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    //    this.mainPresenter = new MainPresenter();

    //    this.mainPresenter.setView(this);
     //   this.mainPresenter.initialize();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof MainFragmentInteractionListener) {
            this.mainFragmentInteractionListener = (MainFragmentInteractionListener) context;
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // this.mainFragmentInteractionListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // this.mainPresenter.destroy();
    }

    @Override
    public void renderInfoToTab(MovieDetailsEntity someMovieData) {

    }

    @Override
    public void showToast(int resourceId) {

    }
}
