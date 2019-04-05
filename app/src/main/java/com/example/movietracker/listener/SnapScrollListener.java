package com.example.movietracker.listener;

import android.os.Handler;

import com.example.movietracker.view.adapter.MovieListAdapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public  class SnapScrollListener extends RecyclerView.OnScrollListener {

    private Fragment fragment;
    private boolean isActionAllowed = true;

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
                    && (fragment instanceof OnLastElementReachedListener) && isActionAllowed) {

                ((OnLastElementReachedListener)fragment).lastElementReached();
            }
        }
    }

    private void triggerListenerWithDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isActionAllowed = false;
                ((OnLastElementReachedListener)fragment).lastElementReached();
                isActionAllowed = true;
            }
        }, 2000);
    }

    private boolean isLastElement(RecyclerView recyclerView) {
        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        return (recyclerView.getAdapter().getItemCount() - 1) == manager.findLastVisibleItemPosition();
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

        return (absoluteTop <= (rowHeight / 2)) ? top - 50 : (rowHeight - absoluteTop) - 50;
    }
}