package com.example.movietracker.interactor;

import io.reactivex.observers.DisposableSingleObserver;

public class DefaultSingleObserver<T> extends DisposableSingleObserver<T> {

    @Override
    public void onSuccess(T t) {
        // intentionally left empty
    }

    @Override
    public void onError(Throwable e) {
        // intentionally left empty
    }

}
