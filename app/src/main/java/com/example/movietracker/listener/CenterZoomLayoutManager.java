package com.example.movietracker.listener;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CenterZoomLayoutManager extends LinearLayoutManager {

    private final float mShrinkAmount = 0.2f;
    private final float mShrinkDistance = 0.8f;

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
            scaleChilds(orientation, recycler, state);
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
            scaleChilds(orientation, recycler, state);

            return scrolled;
        } else {
            return 0;
        }
    }

    private void scaleChilds(int orientation, RecyclerView.Recycler recycler, RecyclerView.State state) {

        float centerLayout;
        float childCenterLayout;

        if (orientation == HORIZONTAL) {
            centerLayout = getWidth() / 2.f;
        } else {
            centerLayout = getHeight() / 2.f;
        }

        float d1 = mShrinkDistance * centerLayout;
        float s1 = 1.f - mShrinkAmount;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);


            if (orientation == HORIZONTAL) {
                childCenterLayout =
                        (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
            } else {
                childCenterLayout =
                        (getDecoratedBottom(child) + getDecoratedTop(child)) / 2.f;
            }

            float d = Math.min(d1, Math.abs(centerLayout - childCenterLayout));
            float scale = 1 + (s1 - 1) * (d) / (d1);

            child.setScaleX(scale);
            child.setScaleY(scale);

            if(childCenterLayout >= (centerLayout  - 80)  && (centerLayout + 80) >= childCenterLayout) {
                child.setElevation(10000);
            } else  if (childCenterLayout < (centerLayout  - 80) ){
                child.setElevation(i);
            } else if ((centerLayout + 80) < childCenterLayout) {
                child.setElevation(-i);
            }

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