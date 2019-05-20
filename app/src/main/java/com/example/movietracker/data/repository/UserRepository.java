package com.example.movietracker.data.repository;

import com.example.movietracker.data.database.MoviesDatabase;
import com.example.movietracker.data.entity.movie.MarkMovieAsFavoriteResultEntity;
import com.example.movietracker.data.entity.movie.MarkMovieAsFavoriteRequestBodyEntity;
import com.example.movietracker.data.entity.movie.MoviesEntity;
import com.example.movietracker.data.entity.user.UserDetailsEntity;
import com.example.movietracker.data.entity.user.UserEntity;
import com.example.movietracker.data.entity.user.UserWithFavoriteMovies;
import com.example.movietracker.data.net.RestClient;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface UserRepository {
    void init(RestClient restClient, MoviesDatabase moviesDatabase);

    Observable<UserEntity> getUser();
    Observable<UserEntity> getUserWithFavorites();
    void addUser(UserEntity userEntity);
    Completable updateUser(UserEntity userEntity);
    Completable deleteMovieFromFavorites(UserWithFavoriteMovies userWithFavoriteMovies);
    Completable syncFavoritesWithServer(UserEntity userEntity);

    Single<UserDetailsEntity> getUserDetailsFromServer(UserEntity userEntity);
    Single<MarkMovieAsFavoriteResultEntity> markAsFavorite(int accountId, MarkMovieAsFavoriteRequestBodyEntity favoriteRequestBody, String sessionId);
    Single<MoviesEntity> getFavoritesFromServer(int accountId, String sessionId, int page);
}
