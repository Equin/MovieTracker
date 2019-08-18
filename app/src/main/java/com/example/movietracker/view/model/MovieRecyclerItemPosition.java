package com.example.movietracker.view.model;

/**
 * Movie recycler item position for saving offset from top and position of clicked movie item.
 */
public class MovieRecyclerItemPosition {

    private int itemPosition;
    private int offset;

    public MovieRecyclerItemPosition() {
        this.itemPosition = 0;
        this.offset = 0;
    }

    public MovieRecyclerItemPosition(int itemPosition, int offset) {
        this.itemPosition = itemPosition;
        this.offset = offset;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setValuesToZero() {
        this.setOffset(0);
        this.setItemPosition(0);
    }
}
