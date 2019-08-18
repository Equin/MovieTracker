package com.example.movietracker.view.helper;

import android.content.res.Configuration;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movietracker.view.custom_view.CenterZoomLayoutManager;

public class RecyclerViewOrientationUtility {

    private RecyclerViewOrientationUtility(){}

    public static void setLayoutManagerToRecyclerView(RecyclerView recyclerView, int orientation) {
        if (recyclerView == null) return;

        RecyclerView.LayoutManager rowLayoutManager = new LinearLayoutManager(
                recyclerView.getContext(), getRecyclerViewOrientation(orientation), false);

        recyclerView.setLayoutManager(rowLayoutManager);
    }

    public static void setCenterZoomLayoutManagerToRecyclerView(RecyclerView recyclerView, int orientation) {
        if (recyclerView == null) return;

        CenterZoomLayoutManager rowLayoutManager = new CenterZoomLayoutManager (
                recyclerView.getContext(), getRecyclerViewOrientation(orientation), false);


       /* recyclerView.setChildDrawingOrderCallback(new RecyclerView.ChildDrawingOrderCallback() {
            @Override
            public int onGetChildDrawingOrder(int childCount, int i) {

                return i;
            }
        });*/
        recyclerView.setLayoutManager(rowLayoutManager);
    }


    public static int getRecyclerViewOrientation(int orientation) {
        return orientation == Configuration.ORIENTATION_PORTRAIT ? RecyclerView.VERTICAL :  RecyclerView.HORIZONTAL;
    }
}
