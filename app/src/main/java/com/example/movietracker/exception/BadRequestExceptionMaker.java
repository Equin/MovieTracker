package com.example.movietracker.exception;

import retrofit2.HttpException;

public class BadRequestExceptionMaker {

    private static final int BAD_REQUEST_CODE = 400;

    private BadRequestExceptionMaker() {
     //
    }

    public static <T extends Throwable> Throwable makeCustomExceptionIfBadRequestOrReturnOriginal(Throwable throwable, Class<T> exception) {
        if (throwable instanceof HttpException && ((HttpException) throwable).code() == BAD_REQUEST_CODE) {
            try {
                return exception.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return throwable;
            }
        }
        return throwable;
    }
}
