package com.example.movietracker.view.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import javax.annotation.Nullable;

public class KeyboardUtility {
    private KeyboardUtility() {}

    public static void showKeyboard(@Nullable  Context context, @Nullable  View view) {
        if (view == null) return;

        view.postOnAnimation(() ->
        {
            if (context != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm!=null) {
                    imm.showSoftInput(view,0);
                }
            }
        });
    }

    public static void hideKeyboard(@Nullable Context context, @Nullable View view) {
        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && view != null)  {
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
        }
    }
}
