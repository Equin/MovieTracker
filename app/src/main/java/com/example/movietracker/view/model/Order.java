package com.example.movietracker.view.model;

public enum Order {

    ASC(true),
    DESC(false);

    private boolean val;

    Order(boolean val) {
        this.val = val;
    }

    public boolean getBoolValue() {
        return val;
    }
}