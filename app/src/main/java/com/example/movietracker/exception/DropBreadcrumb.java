package com.example.movietracker.exception;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.functions.Function;

public final class DropBreadcrumb<T> implements ObservableTransformer<T, T> {
    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        final BreadcrumbException breadcrumb = new BreadcrumbException();
        return upstream.onErrorResumeNext(new Function<Throwable, ObservableSource<? extends T>>() {
            @Override
            public ObservableSource<? extends T> apply(Throwable throwable) throws Exception {
                throw new CompositeException(throwable, breadcrumb);
            }
        });
    }
}

