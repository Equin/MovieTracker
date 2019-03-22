package com.example.movietracker.interactor;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class DefaultObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(@NonNull T t) {
        // intentionally left empty
    }

    @Override
    public void onError(@NonNull Throwable e) {
        // intentionally left empty
    }

    @Override
    public void onComplete() {
        // intentionally left empty
    }
}
