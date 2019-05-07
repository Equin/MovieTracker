package com.example.movietracker.view.helper;

import android.content.res.Configuration;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewOrientationUtility {

    private RecyclerViewOrientationUtility(){}

    public static void setLayoutManagerToRecyclerView(RecyclerView recyclerView, int orientation) {
        if (recyclerView == null) return;

        RecyclerView.LayoutManager rowLayoutManager = new LinearLayoutManager(
                recyclerView.getContext(), getRecyclerViewOrientation(orientation), false);

        recyclerView.setLayoutManager(rowLayoutManager);
    }

    public static int getRecyclerViewOrientation(int orientation) {
        return orientation == Configuration.ORIENTATION_PORTRAIT ? RecyclerView.VERTICAL :  RecyclerView.HORIZONTAL;
    }
}
