package com.example.movietracker.view.custom_view;

public class Option {

    private boolean isSelected;
    private String name;
    private FilterAlertDialog.Order sortOrder;
    private int radioButtonCheckId;

    public Option() {

    }

    public Option(boolean isSelected, String name, FilterAlertDialog.Order sortOrder) {
        this.isSelected = isSelected;
        this.name = name;
        this.sortOrder = sortOrder;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FilterAlertDialog.Order getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(FilterAlertDialog.Order sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getRadioButtonCheckId() {
        return radioButtonCheckId;
    }

    public void setRadioButtonCheckId(int radioButtonCheckId) {
        this.radioButtonCheckId = radioButtonCheckId;
    }
}