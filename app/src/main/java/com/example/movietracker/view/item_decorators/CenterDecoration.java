package com.example.movietracker.view.item_decorators;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import androidx.annotation.Px;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.State;
import org.jetbrains.annotations.NotNull;


public  class CenterDecoration extends ItemDecoration {
    private int firstViewWidth;
    private int firstViewHeight;
    private int lastViewHeight;
    private int lastViewWidth;
    private int coverOffset;
    private int spacing;

    public CenterDecoration(int spacing, int coverOffset) {
        this.spacing = spacing;
        this.coverOffset = coverOffset;
        this.firstViewWidth = -1;
        this.firstViewHeight = -1;
        this.lastViewWidth = -1;
        this.lastViewHeight = -1;
    }

    public void getItemOffsets(@NotNull Rect outRect, @NotNull final View view, @NotNull final RecyclerView parent, @NotNull State state) {
        super.getItemOffsets(outRect, view, parent, state);

        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            int adapterPosition = ((RecyclerView.LayoutParams)layoutParams).getViewAdapterPosition();
            LinearLayoutManager parentLinearLayoutManager = (LinearLayoutManager)parent.getLayoutManager();
            if (adapterPosition == 0) {
                decorateFirstElement(outRect, view, parent, parentLinearLayoutManager);
            } else if (adapterPosition == parentLinearLayoutManager.getItemCount() - 1) {
                decorateLastElement(outRect, view, parent, parentLinearLayoutManager);
            } else {
                decorateCenterElements(outRect, parentLinearLayoutManager);
            }
        }
    }

    /**
     * setting offsets for center elements, so neighbour elements will cover each other
     * @param outRect
     * @param parentLinearLayoutManager
     */
    private void decorateCenterElements(@NotNull Rect outRect, LinearLayoutManager parentLinearLayoutManager) {
        if(parentLinearLayoutManager.getOrientation() == RecyclerView.HORIZONTAL) {
            outRect.left = coverOffset;
            outRect.right = coverOffset;
            outRect.top = Math.abs(this.spacing) / 3;
            outRect.bottom = Math.abs(this.spacing) / 3;

        } else {
            outRect.left = this.spacing / 3;
            outRect.right = this.spacing / 3;
            outRect.top = coverOffset;
            outRect.bottom = coverOffset;
        }
    }

    /**
     * setting offsets for last element... bottom/top and left/right offset (for diff orientation) setting like (middle of screen - middle of itemView).
     * so last element will be in the center of screen.
     * @param outRect
     * @param view
     * @param parent
     * @param parentLinearLayoutManager
     */
    private void decorateLastElement(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, LinearLayoutManager parentLinearLayoutManager) {
        if (view.getWidth() != this.lastViewWidth || view.getHeight() != this.lastViewHeight) {
            invalidateDecorationOnPreDraw(view, parent);
        }

        if(parentLinearLayoutManager.getOrientation() == RecyclerView.HORIZONTAL) {
            this.lastViewWidth = view.getWidth();
            outRect.right = parent.getWidth() / 2 - view.getWidth() / 2;
            outRect.left = this.spacing / 2;
        } else {
            this.lastViewHeight = view.getHeight();
            outRect.top = this.spacing / 2;
            outRect.bottom =  parent.getHeight() / 2 - view.getHeight() / 2;
        }
    }

    /**
     *  * setting offsets for first element... bottom/top and left/right offset (for diff orientation) setting like (middle of screen - middle of itemView)
     *  so first element will be in the center of screen.
     * @param outRect
     * @param view
     * @param parent
     * @param parentLinearLayoutManager
     */
    private void decorateFirstElement(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, LinearLayoutManager parentLinearLayoutManager) {
        if (view.getWidth() != this.firstViewWidth || view.getHeight() != this.firstViewHeight) {
            invalidateDecorationOnPreDraw(view, parent);
        }

        this.firstViewHeight = view.getHeight();
        if(parentLinearLayoutManager.getOrientation() == RecyclerView.HORIZONTAL) {
            this.firstViewWidth = view.getWidth();
            outRect.left = parent.getWidth() / 2 - view.getWidth() / 2;
            if (parentLinearLayoutManager.getItemCount() > 1) {
                outRect.right = coverOffset;
            } else {
                outRect.right = outRect.left;
            }
        } else {
            this.firstViewHeight = view.getHeight();
            outRect.top = parent.getHeight() / 2 - view.getHeight() / 2;
            if (parentLinearLayoutManager.getItemCount() > 1) {
                outRect.bottom = coverOffset;
            } else {
                outRect.bottom = outRect.top;
            }
        }
    }

    private void invalidateDecorationOnPreDraw(@NotNull View view, @NotNull RecyclerView parent) {
        view.getViewTreeObserver().addOnPreDrawListener((new OnPreDrawListener() {
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                parent.invalidateItemDecorations();
                return true;
            }
        }));
    }
}
