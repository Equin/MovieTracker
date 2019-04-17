package com.example.movietracker.model.model_impl;

import com.example.movietracker.data.entity.UserEntity;
import com.example.movietracker.data.entity.UserWithFavoriteMovies;
import com.example.movietracker.data.repository.MovieRepository;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.model.ModelContract;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class UserModelImpl implements ModelContract.UserModel {

    private final MovieRepository movieRepository;

    public UserModelImpl() {
        this.movieRepository = ClassProvider.movieRepository;
    }


    @Override
    public Observable<UserEntity> getUser() {
        return this.movieRepository.getUser();
    }

    @Override
    public Observable<UserEntity> getUserWithFavorites() {
        return this.movieRepository.getUserWithFavorites();
    }

    @Override
    public void addUser(UserEntity userEntity) {
        this.movieRepository.addUser(userEntity);
    }

    @Override
    public Completable updateUser(UserEntity userEntity) {
        return this.movieRepository.updateUser(userEntity);
    }

    @Override
    public Completable deleteUserFromFavorites(UserWithFavoriteMovies userWithFavoriteMovies) {
        return this.movieRepository.deleteMovieFromFavorites(userWithFavoriteMovies);
    }
}
