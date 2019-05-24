package com.example.movietracker.view.custom_view;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CenterZoomLayoutManager extends LinearLayoutManager {

    public static final int CENTER_GAP_INT = 80;
    //scaling percent 20%
    private final float shrinkAmount = 0.2f;
    //item view will be %20 smaller int 80% distance from center to edge
    private final float shrinkDistance = 0.8f;

    public CenterZoomLayoutManager(Context context) {
        super(context);
    }

    public CenterZoomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == VERTICAL) {
            int scrolled = super.scrollVerticallyBy(dy, recycler, state);
            scaleChilds(orientation);
            return scrolled;
        } else {
            return 0;
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            scaleChilds(orientation);

            return scrolled;
        } else {
            return 0;
        }
    }

    private void scaleChilds(int orientation) {

        float centerLayout;
        float childCenterLayout;

        if (orientation == HORIZONTAL) {
            centerLayout = getWidth() / 2.f;
        } else {
            centerLayout = getHeight() / 2.f;
        }

        float scaleDistance = shrinkDistance * centerLayout;
        float s1 = 1.f - shrinkAmount;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (orientation == HORIZONTAL) {
                childCenterLayout =
                        (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
            } else {
                childCenterLayout =
                        (getDecoratedBottom(child) + getDecoratedTop(child)) / 2.f;
            }

            float distance = Math.min(scaleDistance, Math.abs(centerLayout - childCenterLayout));
            float scale = 1 + (s1 - 1) * (distance) / (scaleDistance);

            child.setScaleX(scale);
            child.setScaleY(scale);
            child.setElevation(scale);

          /*  if(childCenterLayout >= (centerLayout  - CENTER_GAP_INT)  && (centerLayout + CENTER_GAP_INT) >= childCenterLayout) {
                child.setElevation(Integer.MAX_VALUE);
            } else  if (childCenterLayout < (centerLayout  - CENTER_GAP_INT) ){
                child.setElevation(i);
            } else if ((centerLayout + CENTER_GAP_INT) < childCenterLayout) {
                child.setElevation(-i);
            }*/
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);

        if (getOrientation() == RecyclerView.VERTICAL) {
            scrollVerticallyBy(0, recycler, state);
        } else {
            scrollHorizontallyBy(0, recycler, state);
        }
    }
}