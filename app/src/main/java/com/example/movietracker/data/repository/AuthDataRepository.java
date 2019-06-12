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

    /**
     * creating user session if user isnt guest and hasnt opened session,
     * else return current session or create empty session if user is guest
     * @return Single<SessionEntity>
     */
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

    /**
     * update user with new session info
     * @param sessionEntity
     * @param userEntity
     */
    private void updateUser(SessionEntity sessionEntity, UserEntity userEntity) {
        userEntity.setSessionId(sessionEntity.getSessionId());
        userEntity.setGuestUser(!sessionEntity.isSuccess());
        userEntity.setHasOpenSession(sessionEntity.isSuccess());
        this.userRepository.updateUser(userEntity).subscribe();
    }

    /**
     * steps to crreate new session
     * 1. get new token
     * 2. validate token with username and password
     * 3. using validated token create new session
     * 4. update user with new session info
     * @param userEntity
     * @return Single<SessionEntity>
     */
    private Single<SessionEntity> newSession(UserEntity userEntity) {
        return this.authApi.getToken().flatMap(requestTokenEntity ->
                this.authApi.validateToken(
                        new ValidateTokenEntity(
                                userEntity.getTMDBUsername(),
                                userEntity.getTMDBPassword(),
                                requestTokenEntity.getRequestToken()
                        )
                ).doOnError((e)-> {
                    updateUser(new SessionEntity(false, ""), userEntity);
                   createUserSession();
                })
                        .flatMap(validatedRequestToken ->
                                this.authApi.createSession(validatedRequestToken.getRequestToken()).doOnSuccess(sessionEntity ->  {
                                            if (!sessionEntity.isSuccess()) {
                                                //TODO - refreshSession few times and return guest session.
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

    /**
     * invalidate session by updating user with empty fields of password, sessionId and setting GuestUser(true), hasOpenSession(false)
     * @param userEntity
     * @return Completable
     */
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
