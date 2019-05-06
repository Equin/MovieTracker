package com.example.movietracker.view.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import com.example.movietracker.R;
import com.example.movietracker.view.helper.pin_code.DigitEditText;

public class CustomPasswordPinEditText extends LinearLayout {

    private final List<DigitEditText> digitEditTexts = new ArrayList<>();

    private final int phoneNumberVerificationCodeLength =
            getIntegerFromResource(R.integer.phone_number_verification_code_length);

    private EditText invisibleCodeEditText;
    private boolean shouldRequestFocus;

    public CustomPasswordPinEditText(Context context) {
        super(context);
        initialize(null);
    }

    public CustomPasswordPinEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public CustomPasswordPinEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(@Nullable AttributeSet set) {

        if(set != null) {
            TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.CustomPasswordPinEditText);
            shouldRequestFocus = ta.getBoolean(R.styleable.CustomPasswordPinEditText_should_request_focus, false);
            ta.recycle();
        }

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setSize(this, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setOnClickListener(view -> requestCodeEditTextFocus());

        setupInvisibleEditText();
        createDigitEditTexts();
        selectDigitAtIndex(0);
    }

    private int getIntegerFromResource(int resourceId) {
        return getResources().getInteger(resourceId);
    }

    private void createDigitEditTexts() {
        for (int i = 1; i <= this.phoneNumberVerificationCodeLength; i++) {
            DigitEditText digitEditText = new DigitEditText(getContext(), isLastDigit(i));
            this.digitEditTexts.add(digitEditText);
            digitEditText.setOnClickListener(view -> requestCodeEditTextFocus());
            addView(digitEditText);
        }
    }

    private void requestCodeEditTextFocus() {
        this.invisibleCodeEditText.requestFocus();
        showKeyboard(this.invisibleCodeEditText);
    }

    private void setupInvisibleEditText() {
        final int inputType =
                getIntegerFromResource(R.integer.phone_number_verification_code_input_type);
        this.invisibleCodeEditText = new EditText(getContext());
        setSize(this.invisibleCodeEditText, 0, 0);
        this.invisibleCodeEditText.setInputType(inputType);
        this.invisibleCodeEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(this.phoneNumberVerificationCodeLength)
        });
        addView(this.invisibleCodeEditText);
        setupEditTextCodeListener(this.invisibleCodeEditText);
        this.invisibleCodeEditText.setId(R.id.invisibleCodeEditText);

        if(shouldRequestFocus) {
            requestCodeEditTextFocus();
        }
    }

    private void setSize(View v, int width, int height) {
        v.setLayoutParams(new LayoutParams(width, height));
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm!=null) {
            imm.showSoftInput(view,0);
        }
    }

    public static void setMargins(View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams p = (MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }

    public String getPasswordValue() {
       return isCodeComplete(invisibleCodeEditText.getText().toString().length()) ? invisibleCodeEditText.getText().toString() : "";
    }

    private boolean isLastDigit(int i) {
        return i == this.phoneNumberVerificationCodeLength;
    }

    private boolean isCodeComplete(int textLengthAfter) {
        return textLengthAfter == phoneNumberVerificationCodeLength;
    }

    private boolean isCodeIncomplete(int textSizeBefore, int size) {
        return textSizeBefore < size;
    }

    private void selectDigitAtIndex(int digitIndex) {
        if (isDigitIndexInvalid(digitIndex)) return;

        getDigitEditTextAt(digitIndex).select();
    }

    private boolean isDigitIndexInvalid(int digitIndex) {
        return digitIndex < 0 || digitIndex >= this.phoneNumberVerificationCodeLength;
    }

    private void deleteDigitAtIndex(int digitIndex) {
        if (isDigitIndexInvalid(digitIndex)) return;

        getDigitEditTextAt(digitIndex).reset();

        if (digitIndex == 0) {
            selectDigitAtIndex(0);
        }
    }

    private DigitEditText getDigitEditTextAt(int index) {
        return this.digitEditTexts.get(index);
    }

    private void clearDigitTextEdits() {
        for (int i = 0; i < this.digitEditTexts.size(); ++i) {
            deleteDigitAtIndex(i);
        }
    }

    private void setupEditTextCodeListener(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            int lastTextLength = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lastTextLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int textLengthAfter = charSequence.length();
                if (textLengthAfter == 0) {
                    clearDigitTextEdits();
                } else if (isCodeIncomplete(lastTextLength, textLengthAfter)) {
                    selectDigitAtIndex(textLengthAfter);
                    String digit = charSequence.subSequence(lastTextLength, textLengthAfter).toString();
                    getDigitEditTextAt(textLengthAfter - 1).setDigit(digit);
                } else {
                    deleteDigitAtIndex(textLengthAfter);
                    if (!isDigitIndexInvalid(textLengthAfter + 1)) {
                        getDigitEditTextAt(textLengthAfter + 1).deselect();
                    }
                    selectDigitAtIndex(textLengthAfter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}