package com.example.movietracker.listener;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public  class SnapScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (RecyclerView.SCROLL_STATE_IDLE == newState) {
            final int scrollDistance = getScrollDistanceOfColumnClosestToTop(recyclerView);
            if (scrollDistance != 0) {
                recyclerView.smoothScrollBy( 0, scrollDistance);
            }
        }
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