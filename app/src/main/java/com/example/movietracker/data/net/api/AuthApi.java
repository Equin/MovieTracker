package com.example.movietracker.data.net.api;

import com.example.movietracker.data.entity.session.RequestTokenEntity;
import com.example.movietracker.data.entity.session.SessionEntity;
import com.example.movietracker.data.entity.session.ValidateTokenEntity;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {

    @GET("authentication/token/new")
    Single<RequestTokenEntity> getToken();

    @POST("authentication/token/validate_with_login")
    Single<RequestTokenEntity> validateToken(@Body ValidateTokenEntity validateTokenEntity);

    @POST("authentication/session/new")
    Single<SessionEntity> createSession(@Query("request_token") String requestToken);
}
