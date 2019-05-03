package com.example.movietracker.view.helper.pin_code;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.movietracker.R;

@SuppressLint("ViewConstructor")
public class DigitEditText extends CutCopyPasteEditText {

    private final boolean isLastDigit;

    public DigitEditText(Context context, boolean isLastDigit) {
        super(context);
        this.isLastDigit = isLastDigit;

        initialize();
    }

    public void select() {
        getBackground().setColorFilter(getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
    }

    public void deselect() {
        getBackground().setColorFilter(getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    }

    private int getColor(int colorResourceId) {
        return getResources().getColor(colorResourceId);
    }

    public boolean setDigit(char c) {
        if (!Character.isDigit(c)) return false;

        setText(Character.toString(c));

        deselect();

        return true;
    }

    public boolean setDigit(String digit) {
        if (digit == null || digit.length() != 1) return false;

        char character = digit.charAt(0);
        return setDigit(character);
    }

    public void reset() {
        setText("");
        deselect();
    }

    private void initialize() {
        final int height =
                getDimensionPixelSize(R.dimen.phone_number_verification_code_edit_text_height);
        final int width =
                getDimensionPixelSize(R.dimen.phone_number_verification_code_edit_text_width);
        final int marginSize =
                getDimensionPixelSize(R.dimen.phone_number_verification_code_edit_text_margins);

        setFocusable(false);
        setGravity(Gravity.CENTER);

        setLayoutParams(new LinearLayout.LayoutParams(width, height));
        reset();

        setMarginLeft(marginSize);

        if (this.isLastDigit) {
            setMarginRight(marginSize);
        }
    }

    private int getDimensionPixelSize(int resourceId) {
        return getResources().getDimensionPixelSize(resourceId);
    }

    public void setMarginLeft(int left) {
        if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) getLayoutParams();
            setMargins(left, p.topMargin, p.rightMargin, p.bottomMargin);
        }
    }

    public void setMarginRight(int right) {
        if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) getLayoutParams();
            setMargins(p.leftMargin, p.topMargin, right, p.bottomMargin);
        }
    }

    private void setMargins(int left, int top, int right, int bottom) {
        if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) getLayoutParams();
            p.setMargins(left, top, right, bottom);
            requestLayout();
        }
    }
}
