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

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter {

    private static final String TAG = MainPresenter.class.getCanonicalName();

    private ModelContract.GenreModel genreModel;
    private ModelContract.UserModel userModel;

    private MainView mainView;
    private Filters filters;

    private GenresEntity genresEntity;
    private UserEntity userEntity;
    private Disposable userDisposable;

    public MainPresenter(MainView mainView, ModelContract.GenreModel genreModel, ModelContract.UserModel userModel, Filters filters) {
        this.mainView = mainView;
        this.genreModel = genreModel;
        this.filters = filters;
        this.userModel = userModel;
        this.userDisposable = new CompositeDisposable();
    }

    public void getGenres() {
        showLoading();
        this.genreModel.getGenres()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleGetGenresObserver());
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
        this.mainView.openMovieListView(this.genresEntity);
    }

    public void onCancelButtonClicked() {
        this.mainView.dismissAllSelections();
    }

    public void onFilterButtonClicked() {
        this.mainView.openAlertDialog();
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
            if(this.userEntity.getPinCode().equals(oldPasswordValue)
                    || this.userEntity.getMasterPinCode().equals(oldPasswordValue)) {
                savePassword(newPasswordValue);
            } else {
                MainPresenter.this.mainView.showToast(R.string.wrong_old_password);
            }
        } else {
            if (oldPasswordValue.equals("")) {
                MainPresenter.this.mainView.showToast(R.string.empty_old_password_field);
            } else {
                MainPresenter.this.mainView.showToast(R.string.empty_new_password_field);
            }
        }
    }

    public void onSaveNewPasswordButtonClicked(String newPasswordValue) {
        if (!newPasswordValue.equals("")) {
            savePassword(newPasswordValue);
        } else {
            MainPresenter.this.mainView.showToast(R.string.main_error);
        }
    }

    public void onPasswordResetMenuItemClicked() {
        if (this.userEntity.getPinCode() == null) {
            this.mainView.openNewPasswordDialog();
        } else {
            this.mainView.openResetPasswordDialog();
        }
    }

    private void showLoading() {
        this.mainView.showLoading();
    }

    private void hideLoading() {
        this.mainView.hideLoading();
    }

    public void onParentalControlSwitchChanged(boolean isChecked) {
        if (this.userEntity == null) return;
        if(isChecked && this.userEntity.isParentalControlEnabled()) {
            return;
        }

        if(this.userEntity.getPinCode() == null) {
            this.mainView.showToast(R.string.hint_to_enable_parent_control);
            this.mainView.openNewPasswordDialog();
        } else  {
            if(isChecked) {
                this.updateParentalControlState(true);
            } else {
                this.mainView.openCheckPasswordDialog();
            }
        }
    }

    private void savePassword(String newPassword) {
        this.userEntity.setPinCode(newPassword);
        this.userEntity.setParentalControlEnabled(true);

        this.userModel.updateUser(userEntity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableSavePasswordObserver());
    }

    private void getUser() {
        this.userDisposable = this.userModel.getUser().
                observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new GetUserObserver());
    }

    private void updateParentalControlState(boolean isChecked) {
        this.userEntity.setParentalControlEnabled(isChecked);
        this.userModel.updateUser(userEntity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableSetParentalControlStateObserver());
    }

    public void onCheckPasswordButtonClicked(String passwordValue) {
        if (this.userEntity.getPinCode().equals(passwordValue)
                || this.userEntity.getMasterPinCode().equals(passwordValue) ) {
            updateParentalControlState(false);
            this.mainView.dismissPasswordDialog();
        } else {
            this.mainView.showToast(R.string.wrong_password);
            this.mainView.setParentalControlEnabled(userEntity.isParentalControlEnabled());
        }
    }

    private class GetUserObserver extends DisposableObserver<UserEntity> {

        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete this.userModel.getUser()");
        }

        @Override
        public void onNext(UserEntity userEntity) {
            MainPresenter.this.userEntity = userEntity;
            MainPresenter.this.mainView.setParentalControlEnabled(
                    MainPresenter.this.userEntity.isParentalControlEnabled());
        }

        @Override
        public void onError(Throwable e) {
            MainPresenter.this.mainView.showToast(R.string.main_error);
            Log.d(TAG, e.getLocalizedMessage());
        }
    }

    private class SingleGetGenresObserver implements SingleObserver<GenresEntity> {
        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Subscribed to this.genreModel.getGenres()");
        }

        @Override
        public void onSuccess(GenresEntity genreList) {
            genresEntity = genreList;
            MainPresenter.this.mainView.renderGenreView(genreList);
            getUser();
            MainPresenter.this.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            MainPresenter.this.mainView.showToast(R.string.main_error);
            MainPresenter.this.hideLoading();
        }
    }

    private class CompletableSavePasswordObserver implements CompletableObserver {

        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Subscribed to  this.userModel.saveUser(userEntity)");
        }

        @Override
        public void onComplete() {
            MainPresenter.this.mainView.dismissPasswordDialog();
            MainPresenter.this.mainView.showToast(R.string.new_password_saved);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            MainPresenter.this.mainView.showToast(R.string.main_error);
        }
    }

    private class CompletableSetParentalControlStateObserver implements CompletableObserver {

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
    }
}

