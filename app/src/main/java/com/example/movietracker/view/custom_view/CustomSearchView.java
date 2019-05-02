package com.example.movietracker.view.custom_view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.view.MovieCardItemDecorator;
import com.example.movietracker.view.adapter.SearchResultMovieListAdapter;


public class CustomSearchView extends SearchView {

    private static final int RECYCLER_VIEW_CARD_ITEM_OFFSET_DPI = 8;

    private RelativeLayout constraintLayout;
    private RecyclerView recyclerView;

    public CustomSearchView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setVisibilityOfSearchResultBox(int visibility) {
        if (this.constraintLayout != null) {
            this.constraintLayout.setVisibility(visibility);
        }
    }

    public void setRecyclerViewAdapter(SearchResultMovieListAdapter recycleViewAdapter) {
        this.recycleViewAdapter = recycleViewAdapter;
    }

    SearchResultMovieListAdapter recycleViewAdapter;
    FrameLayout.LayoutParams linearLayoutParams;
    Rect rect;

    public void createSearchResultRecyclerViewBox() {
        if (constraintLayout != null) return;
        rect = new Rect();
        View view = (View)this.getParent();
        view.getGlobalVisibleRect(rect);
        constraintLayout = new RelativeLayout(getContext());
        recyclerView = new RecyclerView(getContext());

        linearLayoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayoutManager rowLayoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);

        recyclerView.setId(R.id.recycler_view_search_results);
        recyclerView.setLayoutParams(linearLayoutParams);
        constraintLayout.addView(recyclerView);

        recyclerView.setAdapter(recycleViewAdapter);
        recyclerView.setLayoutManager(rowLayoutManager);
        recyclerView.addItemDecoration(new MovieCardItemDecorator(RECYCLER_VIEW_CARD_ITEM_OFFSET_DPI));

        linearLayoutParams.setMargins(convertDpToPixel(14, getContext()), rect.bottom, convertDpToPixel(14, getContext()),0);

        constraintLayout.setBackgroundColor(getContext().getResources().getColor(R.color.fragment_movie_details_info_background));
        constraintLayout.setLayoutParams(linearLayoutParams);
        ViewGroup viewGroup = (ViewGroup) this.getRootView();

        viewGroup.setClickable(true);
        view.setDuplicateParentStateEnabled(true);
        viewGroup.addView(constraintLayout);


        ImageView closeButton = this.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            EditText searchEditText = findViewById(R.id.search_src_text);
            searchEditText.setText("");
            recycleViewAdapter.reloadWithNewResults(new MoviesEntity());
            constraintLayout.setVisibility(GONE);
        });

        constraintLayout.setVisibility(GONE);
        adjustSearchResultBoxToVisibleScreen();
    }

int oldSize;
    int newSize;


public void adjustSearchResultBoxToVisibleScreen() {
    this.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
        Rect r = new Rect();
        getWindowVisibleDisplayFrame(r);
        newSize = r.bottom - rect.bottom;

        if (oldSize != newSize) {
            oldSize = newSize;
            linearLayoutParams.height = oldSize;
            constraintLayout.setLayoutParams(linearLayoutParams);
            constraintLayout.requestLayout();
        }
    });
}


    @Override
    public void onActionViewExpanded() {
        super.onActionViewExpanded();
        createSearchResultRecyclerViewBox();
    }

    @Override
    public void onActionViewCollapsed() {
        super.onActionViewCollapsed();
        setVisibilityOfSearchResultBox(GONE);
        this.removeView(constraintLayout);
    }

    public static int convertPixelsToDp(int px, Context context) {
        return px / (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static int convertDpToPixel(int dp, Context context){
        return dp * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
