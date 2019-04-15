package com.example.movietracker.presenter;

import android.util.Log;

import com.example.movietracker.AndroidApplication;
import com.example.movietracker.R;
import com.example.movietracker.data.entity.UserEntity;
import com.example.movietracker.view.model.Filters;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.view.contract.MainView;
import com.example.movietracker.view.model.Option;

import java.io.IOException;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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
    private Disposable userDisposable = new CompositeDisposable();

    /**
     * Instantiates a new Main presenter.
     *
     * @param mainView   the main view implemented by MainFragment
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
//TODO Need to check getUser() with getGenres chain
    public void getUser() {
        showLoading();
        this.userDisposable = this.userModel.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(userEntity1 -> {this.userEntity = userEntity1;
                    MainPresenter.this.setParentalControlEnabled(
                            userEntity1.isParentalControlEnabled());
                    return userEntity1;
                })
                .observeOn(Schedulers.io())
                .flatMapSingle(userEntity1 ->
                        this.genreModel.getGenres())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getGenresObserver);
    }

    @Override
    public void destroy() {
        this.mainView = null;
        this.filters = null;
        this.genresEntity = null;
        this.genreModel = null;

        if (!this.userDisposable.isDisposed()) {
            this.userDisposable.dispose();
        }
    }

    public void onSearchButtonClicked(Option option) {
        this.filters.setPage(1);
        this.filters.setIncludeAdult(!this.userEntity.isParentalControlEnabled());
        this.filters.setSortBy(option.getSortBy().getSearchName());
        this.filters.setOrder(option.getSortOrder());
        this.openMovieListView(this.genresEntity);
    }

    public void onFavoriteMenuItemClicked() {
        if (this.mainView != null) {
            this.mainView.openFavoriteMoviesList(this.genresEntity);
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

    public void onGenreChecked(String text, boolean isChecked) {
        for (int i = 0; i < this.genresEntity.getGenres().size(); i++) {
            if (this.genresEntity.getGenres().get(i).getGenreName()
                    .equals(text)) {
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

    public void onSaveResetedPasswordButtonClicked(String oldPasswordValue, String newPasswordValue) {
        if (!oldPasswordValue.equals("") && !newPasswordValue.equals("")) {
            if(this.userEntity.getPassword().equals(oldPasswordValue)
                    || this.userEntity.getMasterPassword().equals(oldPasswordValue)) {
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
        if (!newPasswordValue.equals("")) {
            savePassword(newPasswordValue);
        } else {
            MainPresenter.this.showToast(R.string.main_error);
        }
    }

    public void onPasswordResetMenuItemClicked() {
        if (this.userEntity.getPassword() == null) {
            this.openNewPasswordDialog();
        } else {
            this.openResetPasswordDialog();
        }
    }

    public void onParentalControlSwitchChanged(boolean isChecked) {
        if (this.userEntity == null) return;
        if(isChecked && this.userEntity.isParentalControlEnabled()) {
            return;
        }
        if (!isChecked && !this.userEntity.isParentalControlEnabled()) {
            return;
        }

        if(this.userEntity.getPassword() == null) {
            this.showToast(R.string.hint_to_enable_parent_control);
            this.openNewPasswordDialog();
        } else  {
            if(isChecked) {
                this.updateParentalControlState(true);
            } else {
                this.openCheckPasswordDialog();
            }
        }
    }

    /**
     * On check password button clicked, when switching off parental control
     */
    public void onCheckPasswordButtonClicked(String passwordValue) {
        if (this.userEntity.getPassword().equals(passwordValue)
                || this.userEntity.getMasterPassword().equals(passwordValue) ) {
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

    private CompletableObserver completableSavePasswordObserver = new CompletableObserver() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Subscribed to  this.userModel.saveUser(userEntity)");
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

    private void dismissPasswordDialog() {
        if (this.mainView != null) {
            this.mainView.dismissPasswordDialog();
        }
    }

    private DisposableObserver<GenresEntity> getGenresObserver = new DisposableObserver<GenresEntity>() {
        @Override
        public void onComplete() {
            Log.d(TAG, "Subscribed to this.genreModel.getGenres()");
        }

        @Override
        public void onNext(GenresEntity genreList) {
            genresEntity = genreList;
            MainPresenter.this.renderGenreView(genreList);
            MainPresenter.this.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            MainPresenter.this.showToast(R.string.main_error);
            MainPresenter.this.hideLoading();
        }
    };

    private CompletableObserver completableSetParentalControlStateObserver = new  CompletableObserver() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Subscribed to this.userModel.saveUser(userEntity) to update parent control state");
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




/*    private class GetUserObserver extends DisposableObserver<UserEntity> {

        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete this.userModel.getUser()");
        }

        @Override
        public void onNext(UserEntity userEntity) {
            MainPresenter.this.userEntity = userEntity;
       //     MainPresenter.this.getGenres();
            MainPresenter.this.mainView.setParentalControlEnabled(
                    MainPresenter.this.userEntity.isParentalControlEnabled());
        }

        @Override
        public void onError(Throwable e) {
            MainPresenter.this.mainView.showToast(R.string.main_error);
            Log.d(TAG, e.getLocalizedMessage());
        }
    }*/

/*    private class SingleGetGenresObserver implements SingleObserver<GenresEntity> {
        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Subscribed to this.genreModel.getGenres()");
        }

        @Override
        public void onSuccess(GenresEntity genreList) {
            genresEntity = genreList;
            MainPresenter.this.mainView.renderGenreView(genreList);
            MainPresenter.this.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            MainPresenter.this.mainView.showToast(R.string.main_error);
            MainPresenter.this.hideLoading();
        }
    }*/

/*    public void getGenres() {
        showLoading();
        this.genreModel.getGenres()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleGetGenresObserver());
    }*/



