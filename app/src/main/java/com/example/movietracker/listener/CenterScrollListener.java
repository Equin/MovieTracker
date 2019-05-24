package com.example.movietracker.listener;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movietracker.view.adapter.MovieListAdapter;

import static com.example.movietracker.listener.RecyclerViewHelper.isActionAllowed;
import static com.example.movietracker.listener.RecyclerViewHelper.isLastElement;

public class CenterScrollListener extends RecyclerView.OnScrollListener {

    private Fragment fragment;
    RecyclerView.LayoutManager layoutManager;

    public CenterScrollListener(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        layoutManager = recyclerView.getLayoutManager();
        if (RecyclerView.SCROLL_STATE_IDLE == newState) {

            if (isLastElement(recyclerView)
                    && (recyclerView.getAdapter() instanceof MovieListAdapter)
                    && (fragment instanceof OnLastElementReachedListener) && isActionAllowed(recyclerView)) {

                ((OnLastElementReachedListener) fragment).lastElementReached();
            }
        }
    }
}
/*

  private float getScrollDistanceOfColumnClosestToCenter(final RecyclerView recyclerView) {
        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        final RecyclerView.ViewHolder firstVisibleRowViewHolder
                = recyclerView.findViewHolderForAdapterPosition(manager.findFirstVisibleItemPosition());

       */
/* if (firstVisibleRowViewHolder == null
                || recyclerView.getAdapter().getItemCount() < 2
                || isLastElement(recyclerView)
                || manager.findFirstCompletelyVisibleItemPosition() == 0) {
            return 0;
        }*//*


      int childCount = recyclerView.getChildCount();
      for(int i =0; i< childCount; i++) {
          View child = recyclerView.getChildAt(i);

                  scrollOfest = distanceToCenter(manager, child, getVerticalHelper(layoutManager));
          }


        return scrollOfest;
    }

    private int  distanceToCenter(
            LinearLayoutManager layoutManager,
            View targetView,
            OrientationHelper  helper){
        int childCenter;
        if (helper == horizontalHelper) {
            childCenter =  (int) (targetView.getX() + targetView.getWidth() / 2);
        } else {
            childCenter = (int)(targetView.getY() + targetView.getHeight() / 2);
        }

        int containerCenter;
        if (layoutManager.getClipToPadding()) {
            containerCenter = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        } else {
            containerCenter = helper.getEnd() / 2;
        }
        return childCenter - containerCenter;
    }

    private OrientationHelper verticalHelper = null;
    private OrientationHelper horizontalHelper = null;

    private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager layoutManager) {
        if (verticalHelper == null || verticalHelper.getLayoutManager() != layoutManager) {
            verticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return verticalHelper;
    }

    private OrientationHelper getHorizontalHelper(
            RecyclerView.LayoutManager layoutManager
    ) {
        if (horizontalHelper == null || horizontalHelper.getLayoutManager() != layoutManager) {
            horizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return horizontalHelper;
    }

}
*/
