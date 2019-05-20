package com.example.movietracker.model.model_impl;

import com.example.movietracker.data.entity.session.SessionEntity;
import com.example.movietracker.data.entity.user.UserEntity;
import com.example.movietracker.data.repository.AuthRepository;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.model.ModelContract;

import io.reactivex.Completable;
import io.reactivex.Single;

public class AuthModelImpl implements ModelContract.AuthModel {

    private final AuthRepository authRepository;

    public AuthModelImpl() {
        this.authRepository = ClassProvider.authRepository;
    }

    @Override
    public Single<SessionEntity> createSession() {
        return this.authRepository.createUserSession();
    }

    @Override
    public Single<SessionEntity> login(UserEntity userEntity) {
        return this.authRepository.login(userEntity);
    }

    @Override
    public Single<SessionEntity> refreshSession(UserEntity userEntity) {
        return this.authRepository.refreshSession(userEntity);
    }

    @Override
    public Completable invalidateSession(UserEntity userEntity) {
       return this.authRepository.invalidateSession(userEntity);
    }
}
