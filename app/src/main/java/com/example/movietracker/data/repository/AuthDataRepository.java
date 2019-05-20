package com.example.movietracker.data.repository;

import com.example.movietracker.data.database.MoviesDatabase;

import com.example.movietracker.data.entity.session.SessionEntity;
import com.example.movietracker.data.entity.user.UserEntity;
import com.example.movietracker.data.entity.session.ValidateTokenEntity;
import com.example.movietracker.data.net.RestClient;
import com.example.movietracker.data.net.api.AuthApi;

import io.reactivex.Completable;
import io.reactivex.Single;

public class AuthDataRepository implements AuthRepository {

    private AuthApi authApi;
    private UserRepository userRepository;

    private AuthDataRepository() {
    }

    private static class SingletonHelper {
        private static final AuthDataRepository INSTANCE = new AuthDataRepository();
    }

    public static AuthDataRepository getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public void init(RestClient restClient, MoviesDatabase moviesDatabase, UserRepository userRepository) {
        this.authApi = restClient.getAuthApi();
        this.userRepository = userRepository;
    }

    @Override
    public Single<SessionEntity> createUserSession() {
        return this.userRepository.getUser().firstOrError().flatMap(userEntity -> {
            if (!userEntity.isGuestUser()) {
                if (userEntity.getSessionId().isEmpty()) {
                    return newSession(userEntity);
                }
                else if (userEntity.isHasOpenSession()) {
                    return Single.just(new SessionEntity(userEntity.isHasOpenSession(), userEntity.getSessionId()));
                }
            }
            return Single.just(new SessionEntity(false, ""));
        });
    }

    private void updateUser(SessionEntity sessionEntity, UserEntity userEntity) {
        userEntity.setSessionId(sessionEntity.getSessionId());
        userEntity.setGuestUser(false);
        userEntity.setHasOpenSession(sessionEntity.isSuccess());
        this.userRepository.updateUser(userEntity).subscribe();
    }

    private Single<SessionEntity> newSession(UserEntity userEntity) {
        return this.authApi.getToken().flatMap(requestTokenEntity ->
                this.authApi.validateToken(
                        new ValidateTokenEntity(
                                userEntity.getTMDBUsername(),
                                userEntity.getTMDBPassword(),
                                requestTokenEntity.getRequestToken()
                        )
                )
                        .flatMap(validatedRequestToken ->
                                this.authApi.createSession(validatedRequestToken.getRequestToken()).doOnSuccess(sessionEntity ->  {
                                    if (sessionEntity.isSuccess()) {
                                      /*  this.userRepository.getUserDetailsFromServer().doOnSuccess(userDetailsEntity -> {
                                            use
                                        })*/
                                    }

                                    updateUser(sessionEntity, userEntity);
                                        }
                                )
                        ));
    }

    @Override
    public Single<SessionEntity> login(UserEntity userEntity) {
        return newSession(userEntity);
    }

    @Override
    public Single<SessionEntity> refreshSession(UserEntity userEntity) {
        return newSession(userEntity);
    }

    @Override
    public Completable invalidateSession(UserEntity userEntity) {
        userEntity.setGuestUser(true);
        userEntity.setTMDBPassword("");
        userEntity.setTMDBUsername("Guest");
        userEntity.setSessionId("");
        userEntity.setHasOpenSession(false);
        return this.userRepository.updateUser(userEntity);
    }
}
