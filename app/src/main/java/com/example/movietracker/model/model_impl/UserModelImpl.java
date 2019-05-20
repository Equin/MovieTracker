package com.example.movietracker.model.model_impl;

import com.example.movietracker.data.entity.movie.MarkMovieAsFavoriteResultEntity;
import com.example.movietracker.data.entity.movie.MarkMovieAsFavoriteRequestBodyEntity;
import com.example.movietracker.data.entity.user.UserDetailsEntity;
import com.example.movietracker.data.entity.user.UserEntity;
import com.example.movietracker.data.entity.user.UserWithFavoriteMovies;
import com.example.movietracker.data.repository.UserRepository;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.model.ModelContract;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class UserModelImpl implements ModelContract.UserModel {

    private final UserRepository userRepository;

    public UserModelImpl() {
        this.userRepository = ClassProvider.userRepository;
    }


    @Override
    public Observable<UserEntity> getUser() {
        return this.userRepository.getUser();
    }

    @Override
    public Observable<UserEntity> getUserWithFavorites() {
        return this.userRepository.getUserWithFavorites();
    }

    @Override
    public Single<UserDetailsEntity> getUserDetails(UserEntity userEntity) {
        return this.userRepository.getUserDetailsFromServer(userEntity);
    }

    @Override
    public void addUser(UserEntity userEntity) {
        this.userRepository.addUser(userEntity);
    }

    @Override
    public Completable updateUser(UserEntity userEntity) {
        return this.userRepository.updateUser(userEntity);
    }

    @Override
    public Completable deleteUserFromFavorites(UserWithFavoriteMovies userWithFavoriteMovies) {
        return this.userRepository.deleteMovieFromFavorites(userWithFavoriteMovies);
    }

    @Override
    public  Observable<MarkMovieAsFavoriteResultEntity> syncFavoritesWithServer(UserEntity userEntity) {
        return this.userRepository.syncFavoritesWithServer(userEntity);
    }

    @Override
    public  Observable<MarkMovieAsFavoriteResultEntity>markAsFavorite(
            int accountId,
            MarkMovieAsFavoriteRequestBodyEntity favoriteRequestBody,
            String sessionId) {
        return this.userRepository.markAsFavorite(accountId, favoriteRequestBody, sessionId);
    }
}
