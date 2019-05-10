package com.example.movietracker.view.contract;

public interface TabLayoutView<V> extends DownloadListenerView {
    void renderInfoToTab(V someMovieData);
    void displayNothingToShowHint();
}
