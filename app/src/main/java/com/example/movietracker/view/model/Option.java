package com.example.movietracker.view.model;

public class Option {

    private boolean isSelected;
    private SortBy sortBy;
    private Order sortOrder;
    private int radioButtonCheckId;

    public Option() {

    }

    public Option(boolean isSelected, SortBy sortBy, Order sortOrder) {
        this.isSelected = isSelected;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    public Order getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Order sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getRadioButtonCheckId() {
        return radioButtonCheckId;
    }

    public void setRadioButtonCheckId(int radioButtonCheckId) {
        this.radioButtonCheckId = radioButtonCheckId;
    }
}