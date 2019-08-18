package com.example.movietracker.data.repository;

import com.example.movietracker.data.database.MoviesDatabase;
import com.example.movietracker.data.entity.session.SessionEntity;
import com.example.movietracker.data.entity.user.UserEntity;
import com.example.movietracker.data.net.RestClient;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface AuthRepository {

    Single<SessionEntity> createUserSession();
    Single<SessionEntity> login(UserEntity userEntity);
    Single<SessionEntity> refreshSession(UserEntity userEntity);
    Completable invalidateSession(UserEntity userEntity);

    void init(RestClient restClient, MoviesDatabase moviesDatabase, UserRepository userRepository);
}
