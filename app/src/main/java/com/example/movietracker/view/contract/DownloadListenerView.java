package com.example.movietracker.view.contract;

public interface DownloadListenerView extends View {
    void onDownloadStarted();
    void onDownloadCompleted();
    void onDownloadFailed();
}
