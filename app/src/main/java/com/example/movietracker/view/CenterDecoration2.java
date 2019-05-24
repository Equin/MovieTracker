package com.example.movietracker.view;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import androidx.annotation.Px;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.State;
import org.jetbrains.annotations.NotNull;


public  class CenterDecoration2 extends ItemDecoration {
    private int firstViewWidth;
    private int firstViewHeight;
    private int lastViewHeight;
    private int lastViewWidth;
    private int lastViewSize;
    private int spacing;

    public CenterDecoration2(@Px int spacing) {
        this.spacing = spacing;
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
            LayoutManager parentLayoutManager = parent.getLayoutManager();
            if (parentLayoutManager != null) {
                LinearLayoutManager parentLinearLayoutManager = (LinearLayoutManager)parentLayoutManager;
                if (adapterPosition == 0) {
                    if (view.getWidth() != this.firstViewWidth) {
                        view.getViewTreeObserver().addOnPreDrawListener((new OnPreDrawListener() {
                            public boolean onPreDraw() {
                                view.getViewTreeObserver().removeOnPreDrawListener(this);
                                parent.invalidateItemDecorations();
                                return true;
                            }
                        }));
                    }

                    this.firstViewHeight = view.getHeight();

                    if(((LinearLayoutManager) parentLayoutManager).getOrientation() == RecyclerView.HORIZONTAL) {
                        this.firstViewWidth = view.getWidth();
                        outRect.left = parent.getWidth() / 2 - view.getWidth() / 2;
                        if (parentLinearLayoutManager.getItemCount() > 1) {
                            outRect.right = this.spacing / 2;
                        } else {
                            outRect.right = outRect.left;
                        }
                    } else {
                        this.firstViewHeight = view.getHeight();
                        outRect.top = parent.getHeight() / 2 - view.getHeight() / 2;
                        if (parentLinearLayoutManager.getItemCount() > 1) {
                            outRect.bottom = this.spacing / 2;
                        } else {
                            outRect.bottom = outRect.top;
                        }
                    }


                } else if (adapterPosition == parentLinearLayoutManager.getItemCount() - 1) {
                    if (view.getWidth() != this.lastViewWidth) {
                        view.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                            public boolean onPreDraw() {
                                view.getViewTreeObserver().removeOnPreDrawListener(this);
                                parent.invalidateItemDecorations();
                                return true;
                            }
                        });
                    }

                    if(((LinearLayoutManager) parentLayoutManager).getOrientation() == RecyclerView.HORIZONTAL) {
                        this.lastViewWidth = view.getWidth();
                        outRect.right = parent.getWidth() / 2 - view.getWidth() / 2;
                        outRect.left = this.spacing / 2;
                    } else {
                        this.lastViewHeight = view.getHeight();
                        outRect.top = this.spacing / 2;
                        outRect.bottom =  parent.getHeight() / 2 - view.getHeight() / 2;
                    }

                } else {
                    outRect.left = this.spacing / 2;
                    outRect.right = this.spacing / 2;
                    outRect.top = -40;
                    outRect.bottom = -40;
                }

            }
        }
    }
}
