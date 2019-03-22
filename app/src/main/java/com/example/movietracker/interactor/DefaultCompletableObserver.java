package com.example.movietracker.interactor;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableCompletableObserver;

public class DefaultCompletableObserver extends DisposableCompletableObserver {
    @Override
    public void onComplete() {
        // intentionally left empty
    }

    @Override
    public void onError(@NonNull Throwable e) {
        // intentionally left empty
    }
}
