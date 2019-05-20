package com.example.movietracker.presenter;

import android.util.Log;

import com.example.movietracker.AndroidApplication;
import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie.MoviesEntity;
import com.example.movietracker.data.entity.session.SessionEntity;
import com.example.movietracker.data.entity.user.UserEntity;
import com.example.movietracker.view.helper.RxDisposeHelper;
import com.example.movietracker.view.model.Filters;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.view.contract.MainView;
import com.example.movietracker.view.model.MarkAsFavoriteResultVariants;
import com.example.movietracker.view.model.Option;
import com.example.movietracker.view.model.UserWithGenresEntity;
import com.jakewharton.rxrelay2.PublishRelay;

import java.util.concurrent.TimeUnit;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Main presenter has logic for main screen with genres and menu
 */
public class MainPresenter extends BasePresenter {

    private static final String TAG = MainPresenter.class.getCanonicalName();

    private ModelContract.GenreModel genreModel;
    private ModelContract.UserModel userModel;
    private ModelContract.MovieModel movieModel;
    private ModelContract.AuthModel authModel;

    private MainView mainView;
    private Filters filters;

    private GenresEntity genresEntity;
    private UserEntity userEntity;
    private Disposable userDisposable;
    private Disposable movieDisposable;

    private PublishRelay<String> searchQueryPublishSubject = PublishRelay.create();

    /**
     * Instantiates a new Main presenter.
     *
     * @param mainView the main view implemented by MainFragment
     */
    public MainPresenter(
            MainView mainView, ModelContract.GenreModel genreModel,
            ModelContract.UserModel userModel, ModelContract.MovieModel movieModel,
            ModelContract.AuthModel authModel, Filters filters) {
        this.mainView = mainView;
        this.genreModel = genreModel;
        this.movieModel = movieModel;
        this.authModel = authModel;
        this.filters = filters;
        this.userModel = userModel;
        configureSearch();
    }

    /**
     * create new session if user isn`t guest and get current session if it is available
     */
    public void createSession() {
        showLoading();
        this.authModel.createSession()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CreateSessionSingleObserver());
    }

    /**
     * Gets user with genres
     */
    public void getUser() {
        this.userDisposable = Observable.zip(
                this.userModel.getUser(),
                this.genreModel.getGenres().toObservable(),
                (userEntity, genresEntity) -> new UserWithGenresEntity(genresEntity, userEntity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GetUserWithGenresObserver());
    }

    @Override
    public void destroy() {
        this.mainView = null;
        this.filters = null;
        this.genresEntity = null;
        this.genreModel = null;
        RxDisposeHelper.dispose(this.userDisposable);
        RxDisposeHelper.dispose(this.movieDisposable);
        RxDisposeHelper.dispose(this.movieDisposable);
    }

    /**
     * setting up filters with options and opening movieListView
     * @param option - options from filterAlertDialog
     */
    public void onSearchButtonClicked(Option option) {
        this.filters = new Filters.FiltersBuilder(this.filters)
                .setPage(1)
                .setIncludeAdult(!this.userEntity.isParentalControlEnabled())
                .setSortBy(option.getSortBy().getSearchName())
                .setOrder(option.getSortOrder())
                .build();
        this.openMovieListView(this.genresEntity);
    }

    /**
     * making request to server and db on search query of searchView text changed.
     *
     * @param newText the new text
     */
    public void onSearchQueryTextChange(String newText) {
        if ("".equals(newText)) {
            this.showSearchResult(new MoviesEntity());
            return;
        }
        newSearchQuery(newText);
    }

    /**
     * changing list of selected genres according to state and genreId
     * @param genreId - id of clicked genre
     * @param isChecked - new state
     */
    public void onGenreChecked(int genreId, boolean isChecked) {
        for (int i = 0; i < this.genresEntity.getGenres().size(); i++) {
            if (this.genresEntity.getGenres().get(i).getGenreId() == genreId) {
                this.genresEntity.getGenres().get(i).setSelected(isChecked);

                if (isChecked) {
                    this.filters.addSelectedGenre(
                            this.genresEntity.getGenres().get(i));
                } else {
                    this.filters.removeUnselectedGenre(
                            this.genresEntity.getGenres().get(i));
                }
            }
        }
    }

    /**
     * saving newPasswordValue if oldPasswordValue equals to current password
     */
    public void onSaveResetedPasswordButtonClicked(String oldPasswordValue, String newPasswordValue) {
        if (!("").equals(oldPasswordValue) && !("").equals(newPasswordValue)) {
            if (oldPasswordValue.equals(this.userEntity.getParentalControlPassword())
                    || oldPasswordValue.equals(this.userEntity.getMasterPassword())) {
                savePassword(newPasswordValue);
            } else {
                MainPresenter.this.showToast(R.string.wrong_old_password);
            }
        } else {
            if (oldPasswordValue.equals("")) {
                MainPresenter.this.showToast(R.string.empty_old_password_field);
            } else {
                MainPresenter.this.showToast(R.string.empty_new_password_field);
            }
        }
    }

    /**
     * saving new password to db
     * @param newPasswordValue
     */
    public void onSaveNewPasswordButtonClicked(String newPasswordValue) {
        if (!("").equals(newPasswordValue)) {
            savePassword(newPasswordValue);
        } else {
            MainPresenter.this.showToast(R.string.empty_password_field);
        }
    }

    /**
     * on password reset menu item clicked
     * opening reset password dialog if current password exists else open new password dialog
     */
    public void onPasswordResetMenuItemClicked() {
        if (this.userEntity.getParentalControlPassword() == null) {
            this.openNewPasswordDialog();
        } else {
            this.openResetPasswordDialog();
        }
    }

    /**
     * changing parent control state according to state only if there is a password,
     * if not open dialog for saving new password
     * @param isChecked - state of parentControlSwitcher
     */
    public void onParentalControlSwitchChanged(boolean isChecked) {
        if (this.userEntity == null
                || (isChecked && this.userEntity.isParentalControlEnabled())
                || (!isChecked && !this.userEntity.isParentalControlEnabled())) {
            return;
        }

        if (this.userEntity.getParentalControlPassword() == null && isChecked) {
            this.showToast(R.string.hint_to_enable_parent_control);
            this.openNewPasswordDialog();
        } else {
            if (isChecked) {
                this.updateParentalControlState(true);
            } else {
                this.openCheckPasswordDialog();
            }
        }
    }

    /**
     * Start or stop background sync according to isChecked param
     *
     * @param isChecked
     */
    public void onBackgroundSyncSwitchChanged(boolean isChecked) {
        if (this.userEntity == null
                || (isChecked && this.userEntity.isBackgroundSyncEnabled())
                || (!isChecked && !this.userEntity.isBackgroundSyncEnabled())) {
            return;
        }

        this.changeBackgroundSyncState(isChecked);
        this.updateBackgroundSyncState(isChecked);
    }

    /**
     * opens favorite movie list view by openFavoriteMoviesListView(this.genresEntity);
     */
    public void onFavoriteMenuItemClicked() {
        if (this.mainView != null) {
            this.mainView.openFavoriteMoviesListView(this.genresEntity);
        }
    }

    /**
     * dismiss all selections from genre view and filter alert dialog
     */
    public void onCancelButtonClicked() {
        if (this.mainView != null) {
            this.mainView.dismissAllSelections();
        }
    }

    /**
     * open filter alert dialog
     */
    public void onFilterButtonClicked() {
        if (this.mainView != null) {
            this.mainView.openAlertDialog();
        }
    }

    /**
     * checking provided password to current and master password
     * if password is correct -> switching off parent control else not
     */
    public void onCheckPasswordButtonClicked(String passwordValue) {
        if (passwordValue.equals(this.userEntity.getParentalControlPassword())
                || passwordValue.equals(this.userEntity.getMasterPassword())) {
            updateParentalControlState(false);
            this.mainView.setParentalControlEnabled(userEntity.isParentalControlEnabled());
            dismissDialog();
        } else {
            this.mainView.showToast(R.string.wrong_password);
            this.mainView.setParentalControlEnabled(userEntity.isParentalControlEnabled());
        }
    }

    /**
     * opens movie details according to movieId
     * @param movieId
     */
    public void onMovieItemClicked(int movieId) {
        this.mainView.openMovieDetailScreen(movieId);
    }

    /**
     * opens login dialog
     */
    public void onLoginMenuItemClicked() {
        if (this.mainView != null) {
            this.mainView.openLoginDialog();
        }
    }

    /**
     * doing logout (invalidating session)
     */
    public void onLogoutMenuItemClicked() {
        logout();
    }

    /**
     * creating new session with username and password
     *
     * @param userName
     * @param password
     */
    public void onLoginButtonClicked(String userName, String password) {
        login(userName, password);
    }

    /**
     * doing log in by username and password (creating new session)
     * @param userName
     * @param password
     */
    private void login(String userName, String password) {
        showLoading();
        userEntity.setTMDBUsername(userName);
        userEntity.setTMDBPassword(password);
        this.authModel.login(userEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoginSingleObserver());
    }

    /**
     *  refreshing session (simply by creating new session)
     */
    private void refreshSession() {
        showLoading();
        this.authModel.refreshSession(userEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CreateSessionSingleObserver());
    }

    /**
     * invalidate session by setting empty values to userEntity
     */
    private void logout() {
        if (this.mainView != null && userEntity != null) {
            this.authModel.invalidateSession(userEntity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableInvalidateSessionObserver());
        }
    }

    /**
     * saving password by updating user with new password
     *
     * @param newPassword
     */
    private void savePassword(String newPassword) {
        this.userEntity.setParentalControlPassword(newPassword);
        this.userEntity.setParentalControlEnabled(true);

        this.userModel.updateUser(userEntity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableSavePasswordObserver());
    }

    /**
     * changing parental controle state according to isCheched
     * @param isChecked
     */
    private void updateParentalControlState(boolean isChecked) {
        this.userEntity.setParentalControlEnabled(isChecked);
        this.userModel.updateUser(userEntity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableSetParentalControlStateObserver());
    }

    /**
     * changing Background movie sync state according to isCheched
     * @param isChecked
     */
    private void updateBackgroundSyncState(boolean isChecked) {
        this.userEntity.setBackgroundSyncEnabled(isChecked);
        this.userModel.updateUser(userEntity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableSetBackgroundSyncStateObserver());
    }

    /**
     * syncing Favorites with server if there is open session
     *
     * @param userEntity
     */
    private void syncFavoriteMoviesWithServer(UserEntity userEntity) {
        if (userEntity.isHasOpenSession()){
            this.userModel.syncFavoritesWithServer(userEntity)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new CompletableFavoriteMoviesSyncObserver());
        }
    }

    private void stopBackgroundSync() {
        if (this.mainView != null) {
            this.mainView.stopBackgroundSync();
        }
    }

    private void startBackgroundSync() {
        if (this.mainView != null) {
            this.mainView.startBackgroundSync();
        }
    }

    /**
     * Start or stop background sync according to isChecked params
     *
     * @param isChecked
     */
    private void changeBackgroundSyncState(boolean isChecked) {
        if (isChecked) {
            this.startBackgroundSync();
        } else {
            this.stopBackgroundSync();
        }
    }

    private void showToast(int resourceId) {
        if (this.mainView != null) {
            this.mainView.showToast(resourceId);
        }
    }

    private void showToast(String resourceId) {
        if (this.mainView != null) {
            this.mainView.showToast(resourceId);
        }
    }

    private void openMovieListView(GenresEntity genresEntity) {
        if (this.mainView != null) {
            this.mainView.openMovieListView(genresEntity);
        }
    }

    private void openResetPasswordDialog() {
        if (this.mainView != null) {
            this.mainView.openResetPasswordDialog();
        }
    }

    private void openNewPasswordDialog() {
        if (this.mainView != null) {
            this.mainView.openNewPasswordDialog();
        }
    }

    private void showLoading() {
        if (this.mainView != null) {
            this.mainView.showLoading();
        }
    }

    private void hideLoading() {
        if (this.mainView != null) {
            this.mainView.hideLoading();
        }
    }

    private void renderGenreView(GenresEntity genreList) {
        if (this.mainView != null) {
            this.mainView.renderGenreView(genreList);
        }
    }

    private void openCheckPasswordDialog() {
        if (this.mainView != null) {
            this.mainView.openCheckPasswordDialog();
        }
    }

    private void showSearchResult(MoviesEntity moviesEntity) {
        if (this.mainView != null) {
            this.mainView.showSearchResult(moviesEntity);
        }
    }

    private void setParentalControlEnabled(boolean parentalControlEnabled) {
        if (this.mainView != null) {
            this.mainView.setParentalControlEnabled(parentalControlEnabled);
        }
    }

    private void setBackgroundSyncEnabled(boolean backgroundSyncEnabled) {
        if (this.mainView != null) {
            this.mainView.setBackgroundSyncEnabled(backgroundSyncEnabled);
        }
    }

    private void dismissDialog() {
        if (this.mainView != null) {
            this.mainView.dismissDialog();
        }
    }

    /**
     * providing search query from customVSearchView to make search by title
     * @param query - search string
     */
    private void newSearchQuery(String query) {
        filters.setIncludeAdult(!this.userEntity.isParentalControlEnabled());
        filters.setSearchQueryByTitle(query);
        searchQueryPublishSubject.accept(query);
    }

    private void setUsernameToHeaderView(String tmdbUsername) {
        if (mainView != null) {
            this.mainView.setUsernameToHeaderView(tmdbUsername);
        }
    }

    private void showLoginMenuItem() {
        if (mainView != null) {
            this.mainView.showLoginMenuItem();
        }
    }

    private void hideLogoutMenuItem() {
        if (mainView != null) {
            this.mainView.hideLogoutMenuItem();
        }
    }

    private void showLogoutMenuItem() {
        if (mainView != null) {
            this.mainView.showLogoutMenuItem();
        }
    }

    private void hideLoginMenuItem() {
        if (mainView != null) {
            this.mainView.hideLoginMenuItem();
        }
    }

    /**
     * hiding LoginMenuItemt and showing LogoutMenuItem
     */
    private void loggedIn() {
        hideLoginMenuItem();
        showLogoutMenuItem();
    }

    /**
     * showing LoginMenuItemt and hiding LogoutMenuItem
     */
    private void loggedOut() {
        showLoginMenuItem();
        hideLogoutMenuItem();
    }

    /**
     * Observer that subscribed to searchQueryPublishSubject(searchQuery),
     * waits 300 milliseconds to get emit,
     * triggers only unique emits
     * and taking only new results from server and forgets the old ones (breaking old requests to server/db)
     */
    private void configureSearch() {
        this.movieDisposable = searchQueryPublishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap(s -> this.movieModel.getMoviesByTitle(filters))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GetMovieByTitleObserver());
    }

    /**
     * syncing favorites with tmdb server
     * onComplete: Log.e
     * onError: Log.e
     */
    private class CompletableFavoriteMoviesSyncObserver implements CompletableObserver {

        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Subscribed to FavoriteMoviesSyncObserver sync proces");
        }

        @Override
        public void onComplete() {
            Log.e(TAG, "onComplete CompletableFavoriteMoviesSyncObserver");
        }

        @Override
        public void onError(Throwable e) {
            if(e.getMessage().contains(String.valueOf(MarkAsFavoriteResultVariants.HTTP_401.getResultCode()))) {
                refreshSession();
            }

            Log.e(TAG, e.getMessage() == null ? "": e.getMessage());
/*            MainPresenter.this.showToast(
                    e.getMessage() == null
                            ? AndroidApplication.getRunningActivity().getApplicationContext().getResources()
                                    .getString(R.string.main_error)
                            : e.getMessage());*/
        }
    }

    /**
     * saving password to db
     * onComplete: closing passwordDialog on successful save.
     */
    private class CompletableSavePasswordObserver implements CompletableObserver {

        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Subscribed to  this.userModel.updateUser(userEntity) to save new password");
        }

        @Override
        public void onComplete() {
            MainPresenter.this.dismissDialog();
            MainPresenter.this.showToast(R.string.new_password_saved);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            MainPresenter.this.showToast(R.string.main_error);
        }
    }

    /**
     * getting session
     * onSuccess: getting user if session is created, and hiding/showing logging/out menu items
     */
    private class CreateSessionSingleObserver implements SingleObserver<SessionEntity> {

        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Subscribed to  this.authModel.createSession()");
        }

        @Override
        public void onSuccess(SessionEntity sessionEntity) {
            getUser();
            if(sessionEntity.isSuccess()) {
                loggedIn();
            } else {
                loggedOut();
                logout();
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            MainPresenter.this.showToast(R.string.main_error);
            MainPresenter.this.hideLoading();
        }
    }

    /**
     * creating new session
     * onSuccess: hiding/showing logging/out menu items and syncing favorites movies with server
     */
    private class LoginSingleObserver implements SingleObserver<SessionEntity> {

        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Subscribed to  this.authModel.createSession()");
        }

        @Override
        public void onSuccess(SessionEntity sessionEntity) {
            if(sessionEntity.isSuccess()) {
                loggedIn();
                MainPresenter.this.hideLoading();
                MainPresenter.this.setUsernameToHeaderView(userEntity.getTMDBUsername());
                MainPresenter.this.showToast(R.string.main_presenter_logged_in);
                MainPresenter.this.dismissDialog();
                syncFavoriteMoviesWithServer(userEntity);
            } else {
                loggedOut();
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            MainPresenter.this.showToast(R.string.main_error);
            MainPresenter.this.hideLoading();
        }
    }

    /**
     * getting userEntity and genreEntity from network/db
     * onNext: setting state of parentControlSwitcher and rendering genresEntity to customGenreView
     */
    private class GetUserWithGenresObserver extends DisposableObserver<UserWithGenresEntity> {
        @Override
        public void onComplete() {
            Log.d(TAG, "Subscribed to this.userModel.getUser() with genres");
        }

        @Override
        public void onNext(UserWithGenresEntity userWithGenresEntity) {
            genresEntity = userWithGenresEntity.getGenresEntity();
            userEntity = userWithGenresEntity.getUserEntity();

            syncFavoriteMoviesWithServer(userEntity);

            MainPresenter.this.setParentalControlEnabled(
                    userEntity.isParentalControlEnabled());

            MainPresenter.this.setBackgroundSyncEnabled(
                    userEntity.isBackgroundSyncEnabled());

            MainPresenter.this.changeBackgroundSyncState(userEntity.isBackgroundSyncEnabled());
            MainPresenter.this.renderGenreView(genresEntity);
            MainPresenter.this.setUsernameToHeaderView(userEntity.getTMDBUsername());
            MainPresenter.this.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            MainPresenter.this.showToast(R.string.main_error);
            MainPresenter.this.hideLoading();
        }
    }

    /**
     * getting movies by title and showing result in customSearchView result box
     * onNext: showing result in customSearchView result box
     */
    private class GetMovieByTitleObserver extends DisposableObserver<MoviesEntity> {

        @Override
        public void onComplete() {
            Log.d(TAG, "Subscribed to getMovieByTitle");
        }

        @Override
        public void onNext(MoviesEntity moviesEntity) {
            MainPresenter.this.showSearchResult(moviesEntity);
            MainPresenter.this.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            MainPresenter.this.showToast(R.string.main_error);
            MainPresenter.this.hideLoading();
        }
    }

    /**
     * invalidating session
     * onComplete: changing user name in header view and hiding logout menu item
     */
    private class CompletableInvalidateSessionObserver implements CompletableObserver {

        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Subscribed to invalidation user session");
        }

        @Override
        public void onComplete() {
            MainPresenter.this.loggedOut();
            MainPresenter.this.setUsernameToHeaderView(userEntity.getTMDBUsername());
            MainPresenter.this.showToast(R.string.main_presenter_logged_out);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            MainPresenter.this.showToast(R.string.main_error);
        }
    }

    /**
     * saving new parentalControl state to db
     * onComplete: showing toast with new state
     */
    private  class  CompletableSetParentalControlStateObserver implements CompletableObserver {

        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Subscribed to this.userModel.updateUser(userEntity) to update parent control state");
        }

        @Override
        public void onComplete() {
            MainPresenter.this.mainView.showToast(
                    AndroidApplication.getRunningActivity().getApplicationContext().getResources()
                            .getString(R.string.parent_control_state) + " "
                            + userEntity.isParentalControlEnabled());
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            MainPresenter.this.showToast(R.string.main_error);
        }
    }

    /**
     * Changing background movie update sync observable
     * onComplete: showing toast with new sync state
     */
    private class CompletableSetBackgroundSyncStateObserver implements CompletableObserver {

        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Subscribed to this.userModel.updateUser(userEntity) to update background sync state");
        }

        @Override
        public void onComplete() {
            MainPresenter.this.mainView.showToast(
                    AndroidApplication.getRunningActivity().getApplicationContext().getResources()
                            .getString(R.string.background_sync_state) + " "
                            + userEntity.isBackgroundSyncEnabled());
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            MainPresenter.this.showToast(R.string.main_error);
        }
    }
}



