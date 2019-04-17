package com.example.movietracker.presenter;

import android.util.Log;

import com.example.movietracker.AndroidApplication;
import com.example.movietracker.R;
import com.example.movietracker.data.entity.UserEntity;
import com.example.movietracker.view.helper.RxDisposeHelper;
import com.example.movietracker.view.model.Filters;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.view.contract.MainView;
import com.example.movietracker.view.model.Option;
import com.example.movietracker.view.model.UserWithGenresEntity;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
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

    private MainView mainView;
    private Filters filters;

    private GenresEntity genresEntity;
    private UserEntity userEntity;
    private Disposable userDisposable;

    /**
     * Instantiates a new Main presenter.
     *
     * @param mainView the main view implemented by MainFragment
     */
    public MainPresenter(MainView mainView, ModelContract.GenreModel genreModel, ModelContract.UserModel userModel, Filters filters) {
        this.mainView = mainView;
        this.genreModel = genreModel;
        this.filters = filters;
        this.userModel = userModel;
    }

    /**
     * Gets user with genres
     */
    public void getUser() {
        showLoading();
       this.userDisposable = Observable.zip(
               this.userModel.getUser(),
               this.genreModel.getGenres().toObservable(),
               (userEntity, genresEntity) -> new UserWithGenresEntity(genresEntity, userEntity))
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeWith(getUserWithGenresObserver);
    }

    @Override
    public void destroy() {
        this.mainView = null;
        this.filters = null;
        this.genresEntity = null;
        this.genreModel = null;
        RxDisposeHelper.dispose(this.userDisposable);
    }

    /**
     * setting up filters with options and opening movieListView
     * @param option - options from filterAlertDialog
     */
    public void onSearchButtonClicked(Option option) {
        this.filters.setPage(1);
        this.filters.setIncludeAdult(!this.userEntity.isParentalControlEnabled());
        this.filters.setSortBy(option.getSortBy().getSearchName());
        this.filters.setOrder(option.getSortOrder());
        this.openMovieListView(this.genresEntity);
    }

    public void onFavoriteMenuItemClicked() {
        if (this.mainView != null) {
            this.mainView.openFavoriteMoviesListView(this.genresEntity);
        }
    }

    public void onCancelButtonClicked() {
        if (this.mainView != null) {
            this.mainView.dismissAllSelections();
        }
    }

    public void onFilterButtonClicked() {
        if (this.mainView != null) {
            this.mainView.openAlertDialog();
        }
    }

    /**
     * changing list of selected genres according to state and genreId
     * @param genreId - id of clicked genre
     * @param isChecked - new state
     */
    public void onGenreChecked(int genreId, boolean isChecked) {
        for (int i = 0; i < this.genresEntity.getGenres().size(); i++) {
            if (this.genresEntity.getGenres().get(i).getGenreId()
                     == genreId) {
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
            if (oldPasswordValue.equals(this.userEntity.getPassword())
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
        if (this.userEntity.getPassword() == null) {
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

        if (this.userEntity.getPassword() == null && isChecked) {
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
     * On check password button clicked, showing when switching off parental control.
     * if password is correct -> switching off parent control else not
     */
    public void onCheckPasswordButtonClicked(String passwordValue) {
        if (passwordValue.equals(this.userEntity.getPassword())
                || passwordValue.equals(this.userEntity.getMasterPassword())) {
            updateParentalControlState(false);
            this.mainView.dismissPasswordDialog();
        } else {
            this.mainView.showToast(R.string.wrong_password);
            this.mainView.setParentalControlEnabled(userEntity.isParentalControlEnabled());
        }
    }

    private void showToast(int resourceId) {
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

    private void savePassword(String newPassword) {
        this.userEntity.setPassword(newPassword);
        this.userEntity.setParentalControlEnabled(true);

        this.userModel.updateUser(userEntity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(completableSavePasswordObserver);
    }

    private void updateParentalControlState(boolean isChecked) {
        this.userEntity.setParentalControlEnabled(isChecked);
        this.userModel.updateUser(userEntity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(completableSetParentalControlStateObserver);
    }

    private void setParentalControlEnabled(boolean parentalControlEnabled) {
        if (this.mainView != null) {
            this.mainView.setParentalControlEnabled(parentalControlEnabled);
        }
    }

    private void dismissPasswordDialog() {
        if (this.mainView != null) {
            this.mainView.dismissPasswordDialog();
        }
    }

    /**
     * saving password to db
     * onComplete: closing passwordDialog on successful save.
     */
    private CompletableObserver completableSavePasswordObserver = new CompletableObserver() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Subscribed to  this.userModel.addUser(userEntity)");
        }

        @Override
        public void onComplete() {
            MainPresenter.this.dismissPasswordDialog();
            MainPresenter.this.showToast(R.string.new_password_saved);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            MainPresenter.this.showToast(R.string.main_error);
        }
    };

    /**
     * getting userEntity and genreEntity from network/db
     * onNext: setting state of parentControlSwitcher and rendering genresEntity to customGenreView
     */
    private DisposableObserver<UserWithGenresEntity> getUserWithGenresObserver = new DisposableObserver<UserWithGenresEntity>() {
        @Override
        public void onComplete() {
            Log.d(TAG, "Subscribed to this.userModel.getUser() with genres");
        }

        @Override
        public void onNext(UserWithGenresEntity userWithGenresEntity) {
            genresEntity = userWithGenresEntity.getGenresEntity();
            userEntity = userWithGenresEntity.getUserEntity();
            MainPresenter.this.setParentalControlEnabled(
                    userEntity.isParentalControlEnabled());
            MainPresenter.this.renderGenreView(genresEntity);
            MainPresenter.this.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            MainPresenter.this.showToast(R.string.main_error);
            MainPresenter.this.hideLoading();
        }
    };

    /**
     * saving new parentalControl state to db
     * onComplete: showing toast with new state
     */
    private CompletableObserver completableSetParentalControlStateObserver = new CompletableObserver() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Subscribed to this.userModel.addUser(userEntity) to update parent control state");
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
            MainPresenter.this.mainView.showToast(R.string.main_error);
        }
    };
}



