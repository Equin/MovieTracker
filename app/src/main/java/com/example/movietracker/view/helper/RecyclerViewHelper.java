package com.example.movietracker.view.helper;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHelper {

    private static final int MAXIMUM_COUNT_OF_MOVIES_PER_PAGE = 20;

    private RecyclerViewHelper(){}

    public static boolean isLastElement(RecyclerView recyclerView) {
        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (manager != null) {
            return (recyclerView.getAdapter().getItemCount() - 1) == manager.findLastVisibleItemPosition();
        }
        return false;
    }

    public static boolean isFirstElement(RecyclerView recyclerView) {
        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (manager != null) {
            return ( manager.findFirstVisibleItemPosition() == 0);
        }
        return false;
    }


    public static boolean isActionAllowed(RecyclerView recyclerView) {
        return (recyclerView.getAdapter().getItemCount() >= MAXIMUM_COUNT_OF_MOVIES_PER_PAGE);
    }

}
