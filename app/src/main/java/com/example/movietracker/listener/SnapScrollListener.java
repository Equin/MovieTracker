package com.example.movietracker.listener;

import com.example.movietracker.view.adapter.MovieListAdapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public  class SnapScrollListener extends RecyclerView.OnScrollListener {

    private static final int TOP_OFFSET = 50;
    private static final int BOTTOM_OFFSET = 70;
    private static final int MAXIMUM_COUNT_OF_MOVIES_PER_PAGE = 20;

    private Fragment fragment;

    public SnapScrollListener(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (RecyclerView.SCROLL_STATE_IDLE == newState) {
            final int scrollDistance = getScrollDistanceOfColumnClosestToTop(recyclerView);
            if (scrollDistance != 0) {
                recyclerView.smoothScrollBy( 0, scrollDistance);
            }

            if(isLastElement(recyclerView)
                    && (recyclerView.getAdapter() instanceof MovieListAdapter)
                    && (fragment instanceof OnLastElementReachedListener) && isActionAllowed(recyclerView)) {

                ((OnLastElementReachedListener)fragment).lastElementReached();
            }
        }
    }

    private boolean isLastElement(RecyclerView recyclerView) {
        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        return (recyclerView.getAdapter().getItemCount() - 1) == manager.findLastVisibleItemPosition();
    }

    private boolean isActionAllowed(RecyclerView recyclerView) {
        return (recyclerView.getAdapter().getItemCount() >= MAXIMUM_COUNT_OF_MOVIES_PER_PAGE);
    }

    private int getScrollDistanceOfColumnClosestToTop(final RecyclerView recyclerView) {
        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        final RecyclerView.ViewHolder firstVisibleRowViewHolder
                = recyclerView.findViewHolderForAdapterPosition(manager.findFirstVisibleItemPosition());

        if (firstVisibleRowViewHolder == null
                || recyclerView.getAdapter().getItemCount() < 2
                || isLastElement(recyclerView)
                || manager.findFirstCompletelyVisibleItemPosition() == 0) {
            return 0;
        }

        final int rowHeight = firstVisibleRowViewHolder.itemView.getMeasuredHeight();
        final int top = firstVisibleRowViewHolder.itemView.getTop();
        final int absoluteTop = Math.abs(top);

        if (rowHeight > manager.getHeight()) {
            return 0;
        }

        return (absoluteTop <= (rowHeight / 2)) ? top - BOTTOM_OFFSET: (rowHeight - absoluteTop) - TOP_OFFSET;
    }
}