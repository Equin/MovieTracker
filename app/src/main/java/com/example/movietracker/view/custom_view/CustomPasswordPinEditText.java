package com.example.movietracker.view.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import com.example.movietracker.R;
import com.example.movietracker.view.helper.KeyboardUtility;
import com.example.movietracker.view.helper.pin_code.DigitEditText;

public class CustomPasswordPinEditText extends LinearLayout {

    private final List<DigitEditText> digitEditTexts = new ArrayList<>();

    private final int pinCodeLength =
            getIntegerFromResource(R.integer.pin_code_length);

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

        createDigitEditTexts();
        setupInvisibleEditText();
    }

    private int getIntegerFromResource(int resourceId) {
        return getResources().getInteger(resourceId);
    }

    /**
     * creating digitEditText according to pin code length
     */
    private void createDigitEditTexts() {
        for (int i = 1; i <= this.pinCodeLength; i++) {
            DigitEditText digitEditText = new DigitEditText(getContext(), isLastDigit(i));
            this.digitEditTexts.add(digitEditText);
            digitEditText.setOnClickListener(view -> requestCodeEditTextFocus());
            addView(digitEditText);
        }
    }

    /**
     * requesting focus for invisibleEditText and showing keyboard
     */
    private void requestCodeEditTextFocus() {
        this.invisibleCodeEditText.requestFocus();
        KeyboardUtility.showKeyboard(getContext(), this.invisibleCodeEditText);
    }

    /**
     * creating invisibleEditText for typing password in it
     */
    private void setupInvisibleEditText() {
        final int inputType =
                getIntegerFromResource(R.integer.refactor_code_input_type);
        this.invisibleCodeEditText = new EditText(getContext());
        setSize(this.invisibleCodeEditText, 0, 0);
        this.invisibleCodeEditText.setInputType(inputType);
        this.invisibleCodeEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(this.pinCodeLength)
        });
        addView(this.invisibleCodeEditText);
        setupEditTextCodeListener(this.invisibleCodeEditText);
        this.invisibleCodeEditText.setImeOptions(EditorInfo.IME_FLAG_NO_FULLSCREEN);
        this.invisibleCodeEditText.setId(R.id.invisibleCodeEditText);
        setupEditTextFocusChangeListener(this.invisibleCodeEditText);

        if(shouldRequestFocus) {
            requestCodeEditTextFocus();
        }
    }

    private void setSize(View v, int width, int height) {
        v.setLayoutParams(new LayoutParams(width, height));
    }

    public static void setMargins(View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams p = (MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }

    /**
     * returning value from invisibleEditText
     * @returns password value
     */
    public String getPasswordValue() {
       return isCodeComplete(invisibleCodeEditText.getText().toString().length()) ? invisibleCodeEditText.getText().toString() : "";
    }

    private boolean isLastDigit(int i) {
        return i == this.pinCodeLength;
    }

    private boolean isCodeComplete(int textLengthAfter) {
        return textLengthAfter == pinCodeLength;
    }

    private boolean isCodeIncomplete(int textSizeBefore, int size) {
        return textSizeBefore < size;
    }

    /**
     * selecting digit at provided index
     * @param digitIndex
     */
    private void selectDigitAtIndex(int digitIndex) {
        if (isDigitIndexInvalid(digitIndex)) return;

        getDigitEditTextAt(digitIndex).select();
    }

    /**
     * deselecting digit at provided index
     * @param digitIndex
     */
    private void deselectDigitAtIndex(int digitIndex) {
        if (isDigitIndexInvalid(digitIndex)) return;

        getDigitEditTextAt(digitIndex).deselect();
    }

    private boolean isDigitIndexInvalid(int digitIndex) {
        return digitIndex < 0 || digitIndex >= this.pinCodeLength;
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

    /**
     * selecting and deselecting pinCodeEditText on focus change according to length of invisibleEditText text length
     * @param editText
     */
    private void setupEditTextFocusChangeListener(EditText editText) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            int  length = editText.getText().toString().length();
            if (hasFocus) {
                if(!isLastDigit(length) ||  length == 0){
                    selectDigitAtIndex(length);
                } else {
                    selectDigitAtIndex(length - 1);
                }
            } else {
                if(!isLastDigit(length) ||  length == 0){
                    deselectDigitAtIndex(length);
                } else {
                    deselectDigitAtIndex(length - 1);
                }
            }
        });
    }

    /**
     * setting digits and selecting next pinCodeEditText on text changes in invisibleEditText
     *
     * @param editText
     */
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