package com.example.movietracker.view.helper;

import io.reactivex.disposables.Disposable;

public class RxDisposeHelper {

    private RxDisposeHelper(){}

    public static void dispose(Disposable disposable) {
        if (disposable!=null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
