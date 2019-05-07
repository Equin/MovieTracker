package com.example.movietracker.view.custom_view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import com.example.movietracker.view.MovieCardItemDecorator;
import com.example.movietracker.view.helper.RecyclerViewOrientationUtility;


public class CustomToolbarSearchView extends SearchView {

    private static final int RECYCLER_VIEW_CARD_ITEM_OFFSET_DPI = 4;

    private RelativeLayout relativeLayoutResultBox;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recycleViewAdapter;
    private FrameLayout.LayoutParams linearLayoutParams;
    private Rect visibleSearchViewRect;
    private ImageView closeButton;
    private  EditText searchEditText;
    private  ImageView searchGoButton;

    private int oldScreenHeight;
    private int newScreenHeight;

    public CustomToolbarSearchView(Context context) {
        super(context);
        initialize();
    }

    public CustomToolbarSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CustomToolbarSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        closeButton = findViewById(R.id.search_close_btn);
        searchEditText = findViewById(R.id.search_src_text);
        searchGoButton = findViewById(R.id.search_go_btn);

        setCloseIconColor(R.color.custom_search_view_close_button_color);
        setGoIconColor(R.color.custom_search_view_go_button_color);
        setSearchEditTextColor(R.color.custom_search_view_text_color);
        setSearchEditTextHintColor(R.color.custom_search_view_hint_color);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setVisibilityOfSearchResultBox(int visibility) {
        if (this.relativeLayoutResultBox != null) {
            this.relativeLayoutResultBox.setVisibility(visibility);
        }
    }

    public void setRecyclerViewAdapter(RecyclerView.Adapter recycleViewAdapter) {
        this.recycleViewAdapter = recycleViewAdapter;
    }

    public void setCloseIconColor(int color) {
        closeButton.setColorFilter(getColor(color));
    }

    public void setGoIconColor(int color) {
        searchGoButton.setColorFilter(getColor(color));
    }

    public void setSearchEditTextColor(int color) {
        searchEditText.setTextColor(getColor(color));
    }

    public void setSearchEditTextHintColor(int color) {
        searchEditText.setHintTextColor(getColor(color));
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
        this.removeView(relativeLayoutResultBox);
    }

    private void createSearchResultRecyclerViewBox() {
        if (relativeLayoutResultBox != null) return;
        visibleSearchViewRect = new Rect();

        View view = (View)this.getParent();
        view.getGlobalVisibleRect(visibleSearchViewRect);

        relativeLayoutResultBox = new RelativeLayout(getContext());
        recyclerView = new RecyclerView(getContext());

        linearLayoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        recyclerView.setId(R.id.recycler_view_search_results);
        recyclerView.setLayoutParams(linearLayoutParams);
        relativeLayoutResultBox.addView(recyclerView);

        RecyclerViewOrientationUtility.setLayoutManagerToRecyclerView(
                this.recyclerView,
                getContext().getResources().getConfiguration().orientation);

        recyclerView.setAdapter(recycleViewAdapter);

        recyclerView.addItemDecoration(new MovieCardItemDecorator(RECYCLER_VIEW_CARD_ITEM_OFFSET_DPI));

        linearLayoutParams.width = ((View)this.getParent()).getWidth() - (convertDpToPixel(14, getContext()) * 2);
        linearLayoutParams.setMargins(convertDpToPixel(14, getContext()), visibleSearchViewRect.top * 2, convertDpToPixel(14, getContext()),0);

        relativeLayoutResultBox.setBackgroundColor(getColor(R.color.custom_search_view_result_box_background));
        relativeLayoutResultBox.setLayoutParams(linearLayoutParams);

        ViewGroup rootView = this.getRootView().findViewById(android.R.id.content);
        rootView.addView(relativeLayoutResultBox);

        closeButton.setOnClickListener(v -> {
            searchEditText.setText("");
            relativeLayoutResultBox.setVisibility(GONE);
        });

        relativeLayoutResultBox.setVisibility(GONE);
        adjustSearchResultBoxToVisibleScreen();
    }

    public void adjustSearchResultBoxToVisibleScreen() {
        this.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect visibleWindowRect = new Rect();
            getWindowVisibleDisplayFrame(visibleWindowRect);
            newScreenHeight = visibleWindowRect.bottom - visibleSearchViewRect.bottom;

            if (oldScreenHeight != newScreenHeight) {
                oldScreenHeight = newScreenHeight;
                linearLayoutParams.height = oldScreenHeight;
                relativeLayoutResultBox.setLayoutParams(linearLayoutParams);
                relativeLayoutResultBox.requestLayout();
            }
        });
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        RecyclerViewOrientationUtility.setLayoutManagerToRecyclerView(
                this.recyclerView,
                newConfig.orientation);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        linearLayoutParams.width = ((View)getParent()).getWidth() - (convertDpToPixel(14, getContext()) * 2);
        relativeLayoutResultBox.requestLayout();
    }

    private int convertDpToPixel(int dp, Context context){
        return dp * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private int getColor(int color) {
        int elementColor;

        try {
            elementColor = getContext().getResources().getColor(color);
        } catch (Resources.NotFoundException e) {
            elementColor = 0xfffffff;
        }

        return elementColor;
    }

    @Override
    public void setLayoutParams(final ViewGroup.LayoutParams params) {
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        super.setLayoutParams(params);
    }
}
