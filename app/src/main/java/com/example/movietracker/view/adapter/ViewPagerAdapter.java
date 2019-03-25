package com.example.movietracker.view.adapter;

import com.example.movietracker.view.fragment.MovieCastTabFragment;
import com.example.movietracker.view.fragment.MovieInfoTabFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"Info", "Cast", "Review", "Video"};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MovieInfoTabFragment();
           case 1:
                return new MovieCastTabFragment();
             case 2:
                return new MovieInfoTabFragment();
            case 3:
                return new MovieInfoTabFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
