package com.example.movietracker.data.net.api;

import com.example.movietracker.data.entity.movie.MarkMovieAsFavoriteResultEntity;
import com.example.movietracker.data.entity.movie.MarkMovieAsFavoriteRequestBodyEntity;
import com.example.movietracker.data.entity.movie.MoviesEntity;
import com.example.movietracker.data.entity.user.UserDetailsEntity;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

    @GET("account")
    Single<UserDetailsEntity> getUserDetails(@Query("session_id") String sessionId);

    @POST("account/{account_id}/favorite")
    Observable<MarkMovieAsFavoriteResultEntity> markAsFavorite(@Path("account_id") int accountId, @Body MarkMovieAsFavoriteRequestBodyEntity favoriteRequestBody, @Query("session_id") String sessionId);

    @GET("account/{account_id}/favorite/movies")
    Observable<MoviesEntity> getFavoriteMoviesFromServer(@Path("account_id") int accountId, @Query("session_id") String sessionId, @Query("page") int page);
}
