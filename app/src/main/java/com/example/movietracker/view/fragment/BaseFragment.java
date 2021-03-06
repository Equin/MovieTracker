package com.example.movietracker.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movietracker.AndroidApplication;
import com.example.movietracker.R;
import com.example.movietracker.presenter.MainPresenter;
import com.example.movietracker.view.custom_view.CustomPasswordPinEditText;
import com.example.movietracker.view.helper.KeyboardUtility;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    private Toast toast;

    @Nullable
    @BindView(R.id.progress_overlay)
    View progressView;

    @Nullable
    @BindView(R.id.cl_backplate)
    ConstraintLayout backPlateConstraintLayout;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AlertDialog alertDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        this.backPlateConstraintLayout = null;
        this.toolbar = null;
        this.progressView = null;
        this.alertDialog = null;
        this.toast = null;
    }

    public void setSupportActionBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(this.toolbar);
    }

    public void setTransparentToolbar() {
        if (this.toolbar != null) {
            this.toolbar.getBackground().setAlpha(0);
        }
    }

    public void setNotTransparentToolbar() {
        if (this.toolbar != null) {
            this.toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_color));
        }
    }

    public void setToolbarTitle(String title) {
        if (this.toolbar != null) {
            this.toolbar.setTitle(title);
        }
    }

    public ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public void showLoading() {
        if (progressViewNotExists())
            return;

        this.backPlateConstraintLayout.setAlpha(0.4f);
        this.progressView.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        if (progressViewNotExists())
            return;

        this.progressView.setVisibility(View.GONE);
    }

    public void showToast(String message) {
        if (this.toast!= null) {
            this.toast.cancel();
        }

        this.toast = Toast.makeText(AndroidApplication.getRunningActivity().getApplicationContext(), message, Toast.LENGTH_SHORT);
        this.toast.show();
    }

    public void showToast(int resourceId) {
        this.showToast(getString(resourceId));
    }

    private boolean progressViewNotExists() {
        return this.progressView == null;
    }

    /**
     * creating AlertDialog for Parental control password reset
     * @param mainPresenter
     */
    protected void createPasswordResetDialog(MainPresenter mainPresenter) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View dialogView =  li.inflate(R.layout.password_reset_dialog_pins, null);
        initializeResetPasswordDialogContent(dialogView, mainPresenter);
        createDialog(dialogView);
    }

    /**
     * creating AlertDialog for Parental control password creating
     * @param mainPresenter
     */
    protected void createNewPasswordDialog(MainPresenter mainPresenter) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View dialogView =  li.inflate(R.layout.password_new_dialog_pins, null);
        initializeNewPasswordDialogContent(dialogView, mainPresenter);
        createDialog(dialogView);
    }

    /**
     * creating AlertDialog for Parental control switching off by checking password
     * @param mainPresenter
     */
    protected void createCheckPasswordDialog(MainPresenter mainPresenter) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View dialogView =  li.inflate(R.layout.password_new_dialog_pins, null);
        initializeCheckPasswordDialogContent(dialogView, mainPresenter);
        createDialog(dialogView);
    }

    /**
     * creating AlertDialog for Login
     * @param mainPresenter
     */
    protected void createLoginDialog(MainPresenter mainPresenter) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View dialogView =  li.inflate(R.layout.login_dialog, null);
        initializeLoginDialogContent(dialogView, mainPresenter);
        createDialog(dialogView);
    }

    private void initializeLoginDialogContent(View dialogCustomVIew, MainPresenter mainPresenter) {
        Button savePassword = dialogCustomVIew.findViewById(R.id.button_login);

        EditText userNameEditText = dialogCustomVIew.findViewById(R.id.editText_login_username);
        EditText passwordEditText = dialogCustomVIew.findViewById(R.id.editText_password);

        userNameEditText.requestFocus();
        KeyboardUtility.showKeyboard(this.getContext(), userNameEditText);

        savePassword.setOnClickListener((click)-> {
            String userName = userNameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            mainPresenter.onLoginButtonClicked(
                    userName,
                    password);
        });
    }

    /**
     * dismissing opened Alert Dialog
     */
    public void dismissDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    /**
     * initializing content for parental control reset password AlertDialog
     * @param dialogCustomVIew
     * @param mainPresenter
     */
    private void initializeResetPasswordDialogContent(View dialogCustomVIew, MainPresenter mainPresenter) {
        Button savePassword = dialogCustomVIew.findViewById(R.id.button_save_password);

        CustomPasswordPinEditText oldPasswordEditText = dialogCustomVIew.findViewById(R.id.customPasswordPinEditText_old_password);
        CustomPasswordPinEditText newPasswordEditText = dialogCustomVIew.findViewById(R.id.customPasswordPinEditText_new_password);

        savePassword.setOnClickListener((click)-> {
            String oldPasswordValue = oldPasswordEditText.getPasswordValue();

            String newPasswordValue = newPasswordEditText.getPasswordValue();

            mainPresenter.onSaveResetedPasswordButtonClicked(
                    oldPasswordValue,
                    newPasswordValue);
        });
    }

    /**
     * initializing content for parental control new password AlertDialog
     * @param dialogCustomVIew
     * @param mainPresenter
     */
    private void initializeNewPasswordDialogContent(View dialogCustomVIew, MainPresenter mainPresenter) {
        Button savePassword = dialogCustomVIew.findViewById(R.id.button_save_password);

        CustomPasswordPinEditText newPasswordEditText = dialogCustomVIew.findViewById(R.id.customPasswordPinEditText_new_password);

        savePassword.setOnClickListener((click)->{
            String newPasswordValue = newPasswordEditText.getPasswordValue();

            mainPresenter.onSaveNewPasswordButtonClicked(
                    newPasswordValue);
        });
    }

    /**
     * initializing content for parental controle check password AlertDialog
     * @param dialogCustomVIew
     * @param mainPresenter
     */
    private void initializeCheckPasswordDialogContent(View dialogCustomVIew, MainPresenter mainPresenter) {
        Button checkPassword = dialogCustomVIew.findViewById(R.id.button_save_password);
        checkPassword.setText(getText(R.string.password_reset_dialog_button_check));

        CustomPasswordPinEditText passwordEditText = dialogCustomVIew.findViewById(R.id.customPasswordPinEditText_new_password);

        checkPassword.setOnClickListener(click -> {
            String passwordValue = passwordEditText.getPasswordValue();
            mainPresenter.onCheckPasswordButtonClicked(
                    passwordValue);
        });
    }

    /**
     * showing dialog with provided view
     * @param dialogCustomVIew
     */
    private void createDialog(View dialogCustomVIew) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);;

        alertDialogBuilder.setView(dialogCustomVIew);

        if(alertDialog == null) {
            alertDialog = alertDialogBuilder.create();
        } else {
            alertDialog.dismiss();
            alertDialog = alertDialogBuilder.create();
        }

        Button cancelButton = dialogCustomVIew.findViewById(R.id.button_cancel_dialog);
        cancelButton.setOnClickListener(click -> alertDialog.dismiss());

        alertDialog.show();
    }
}
