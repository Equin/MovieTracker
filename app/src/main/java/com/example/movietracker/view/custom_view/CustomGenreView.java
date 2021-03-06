package com.example.movietracker.view.custom_view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.google.common.collect.Lists;

import java.util.List;

public class CustomGenreView extends ViewGroup {

    private static final int COUNT_OF_BUTTONS_PER_ROW = 3;
    private static final int COUNT_OF_BUTTONS_PER_ROW_LANDSCAPE = 5;

    public CustomGenreView(Context context) {
        this(context, null);
    }

    public CustomGenreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomGenreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }
    }

    /**
     * creating container and adding in it one more container with buttons (3 or 5 buttons per row according to orientation)
     *  setting title of button and its state according to genresEntity and selectedGenresIds
     *  setting onCheckedChangeListener
     *
     * @param genresEntity
     * @param onCheckedChangeListener
     * @param selectedGenresIds
     */
    public void renderGenreView(GenresEntity genresEntity,
                                CompoundButton.OnCheckedChangeListener onCheckedChangeListener,
                                List<Integer> selectedGenresIds) {
        removeAllViews();

        int buttonsPerRow;

        if  (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            buttonsPerRow = COUNT_OF_BUTTONS_PER_ROW_LANDSCAPE;
        } else {
            buttonsPerRow = COUNT_OF_BUTTONS_PER_ROW;
        }

        List<List<GenreEntity>> genres = Lists.partition(genresEntity.getGenres(), buttonsPerRow);

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        linearLayoutParams.setMargins(5,5,5,5);
        linearLayoutParams.gravity = Gravity.CENTER;
        for(int j = 0; j < genres.size(); j++ ) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            for(int i = 0; i<genres.get(j).size(); i++) {
                String title = genres.get(j).get(i).getGenreName();

                ToggleButton toggleButton = new ToggleButton(getContext());

                toggleButton.setText(title);
                toggleButton.setTextOff(title);
                toggleButton.setTextOn(title);

                toggleButton.setAllCaps(false);

                toggleButton.setChecked(genres.get(j).get(i).isSelected());
                toggleButton.setTag(R.id.tag_int_genre_id, genres.get(j).get(i).getGenreId());

                if(selectedGenresIds.isEmpty()) {
                    toggleButton.setChecked(genres.get(j).get(i).isSelected());
                } else {
                    if (selectedGenresIds.contains(genres.get(j).get(i).getGenreId())) {
                        toggleButton.setChecked(true);
                    } else {
                        toggleButton.setChecked(false);
                    }
                }

                toggleButton.setPadding(25, 0, 25, 0);

                toggleButton.setBackgroundDrawable(getContext().getDrawable(R.drawable.main_button_background));

                toggleButton.setOnCheckedChangeListener(onCheckedChangeListener);
                toggleButton.setLayoutParams(linearLayoutParams);

                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.addView(toggleButton, i);
            }

            addView(linearLayout, j);
        }
    }

    /**
     * removing selections from Genre toggle buttons
     */
    public void dismissSelections() {
        for(int i = 0; i < getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) getChildAt(i);
            for(int j = 0; j < linearLayout.getChildCount(); j++) {
                ToggleButton toggleButton = (ToggleButton) linearLayout.getChildAt(j);
                toggleButton.setChecked(false);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int curWidth;
        int curHeight;
        int curLeft;
        int curTop;
        int maxHeight;
        //get the available size of child view
        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;
        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                return;
            //Get the maximum size of the child
            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();
            //wrap is reach to the end
            if (curLeft + curWidth >= childRight) {
                curLeft = childLeft;
                curTop += maxHeight;
                maxHeight = 0;
            }

            // do the layout
            // child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
            child.layout(curLeft, curTop, childWidth, curTop + curHeight);

            int finalCurWidth = curWidth;
            child.postOnAnimation(()-> {
                child.setPadding(((childRight - finalCurWidth) / 10), 0, (childRight - finalCurWidth) / 10, 0);
            });

            //store the max height
            if (maxHeight < curHeight)
                maxHeight = curHeight;
            curLeft += curWidth;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int count = getChildCount();
        // Measurement will ultimately be computing these values.
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int mLeftWidth = 0;
        int rowCount = 0;
        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            // Measure the child.
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            maxWidth += Math.max(maxWidth, child.getMeasuredWidth());
            mLeftWidth += child.getMeasuredWidth();
            if ((mLeftWidth / 10) > rowCount) {
                maxHeight += child.getMeasuredHeight();
                rowCount++;
            } else {
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }
        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
    }
}
