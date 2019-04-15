package com.example.movietracker.data.net.interceptor;

import com.example.movietracker.data.net.constant.NetConstant;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * The type Query parameter interceptor, adding api_key for all retrofit calls.
 */
public class QueryParameterInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", NetConstant.API_KEY)
                .build();

        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
