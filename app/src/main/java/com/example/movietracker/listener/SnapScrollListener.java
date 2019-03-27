package com.example.movietracker.listener;

import com.example.movietracker.view.adapter.MovieListAdapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public  class SnapScrollListener extends RecyclerView.OnScrollListener {

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
                    && (fragment instanceof OnLastElementReachedListener)) {

                ((OnLastElementReachedListener)fragment).lastElementReached();
            }
        }
    }

    private boolean isLastElement(RecyclerView recyclerView) {
        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        return (recyclerView.getAdapter().getItemCount()-1) == manager.findLastVisibleItemPosition();
    }

    private int getScrollDistanceOfColumnClosestToTop(final RecyclerView recyclerView) {
        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        final RecyclerView.ViewHolder firstVisibleRowViewHolder = recyclerView.findViewHolderForAdapterPosition(manager.findFirstVisibleItemPosition());

        if (firstVisibleRowViewHolder == null || recyclerView.getAdapter().getItemCount() < 2) {
            return 0;
        }

        if(manager.findLastCompletelyVisibleItemPosition() == recyclerView.getAdapter().getItemCount()-1) {
            return 0;
        }

        final int rowHeight = firstVisibleRowViewHolder.itemView.getMeasuredHeight() - 100;
        final int top = firstVisibleRowViewHolder.itemView.getTop() -50;
        final int absoluteTop = Math.abs(top);
        return (absoluteTop <= (rowHeight / 2)) ? top : (rowHeight - absoluteTop) + 100;
    }
}