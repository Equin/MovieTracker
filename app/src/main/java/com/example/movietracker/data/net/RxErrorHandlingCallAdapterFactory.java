package com.example.movietracker.data.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

import com.example.movietracker.exception.NoNetworkException;

public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {

    private final RxJava2CallAdapterFactory original;

    private RxErrorHandlingCallAdapterFactory() {
        original = RxJava2CallAdapterFactory.create();
    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(
            @NonNull Type returnType,
            @NonNull Annotation[] annotations,
            @NonNull Retrofit retrofit) {
        return new RxCallAdapterWrapper(this.original.get(returnType, annotations, retrofit));
    }

    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }

    private static class RxCallAdapterWrapper implements CallAdapter<Observable<?>, Object> {

        private final CallAdapter<?, ?> wrapped;

        RxCallAdapterWrapper(CallAdapter<?, ?> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object adapt(@NonNull Call call) {
            Object result = this.wrapped.adapt(call);
            if (result instanceof Observable) {
                return ((Observable<?>) result).onErrorResumeNext((error) -> {
                    return Observable.error(asCustomException(error));
                });
            } else {
                return ((Completable) result).onErrorResumeNext((error) ->
                        Completable.error(asCustomException(error))
                );
            }
        }

        private Throwable asCustomException(Throwable throwable) {
            if (throwable instanceof IOException) {
                return new NoNetworkException(throwable);
            }

            return throwable;
        }
    }
}
