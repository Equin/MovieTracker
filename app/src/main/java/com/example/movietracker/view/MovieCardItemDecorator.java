package com.example.movietracker.view;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The Movie card item decorator.
 */
public class MovieCardItemDecorator extends RecyclerView.ItemDecoration {
    private int offset;

    public MovieCardItemDecorator(int offsetDPI) {
        this.offset = offsetDPI;
    }

    /**
     * setting offset for recycler view item, if first visible item is first item it sets top offset to offsetDP value and other to offsetDP/2
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final int offsetDP = offset * (int)view.getContext().getResources().getDisplayMetrics().density;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int firstVisibleItemPosition = linearLayoutManager != null ? linearLayoutManager.findFirstCompletelyVisibleItemPosition() : 0;
        if (firstVisibleItemPosition == 0) {
            outRect.top = offsetDP;
            outRect.bottom = offsetDP/2;
        } else {
            outRect.bottom = offsetDP/2;
            outRect.top = offsetDP/2;
        }
        outRect.left = offsetDP;
        outRect.right = offsetDP;
    }
}
