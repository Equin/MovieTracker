package com.example.movietracker.view.model;

import com.example.movietracker.data.entity.UserEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;

public class UserWithGenresEntity {
    private GenresEntity genresEntity;
    private UserEntity userEntity;

    public UserWithGenresEntity(GenresEntity genresEntity, UserEntity userEntity) {
        this.genresEntity = genresEntity;
        this.userEntity = userEntity;
    }

    public GenresEntity getGenresEntity() {
        return genresEntity;
    }

    public void setGenresEntity(GenresEntity genresEntity) {
        this.genresEntity = genresEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}