package com.example.movietracker.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.movietracker.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    public void setSupportActionBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(this.toolbar);
    }

    public void setTransparentToolbar() {
        if (this.toolbar != null) {
            this.toolbar.getBackground().setAlpha(0);
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

        this.toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        this.toast.show();
    }

    public void showToast(int resourceId) {
        this.showToast(getString(resourceId));
    }

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm!=null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(),0);
        }
    }

    private boolean progressViewNotExists() {
        return this.progressView == null;
    }
}
