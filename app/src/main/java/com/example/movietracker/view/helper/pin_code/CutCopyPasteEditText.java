package com.example.movietracker.view.helper.pin_code;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class CutCopyPasteEditText extends AppCompatEditText {

    public interface OnCutCopyPasteListener {
        void onCut(AppCompatEditText editText);
        void onCopy(AppCompatEditText editText);
        void onPaste(AppCompatEditText editText);
    }

    private OnCutCopyPasteListener cutCopyPasteListener;

    public void setOnCutCopyPasteListener(OnCutCopyPasteListener listener) {
        this.cutCopyPasteListener = listener;
    }

    public CutCopyPasteEditText(Context context) {
        super(context);
    }

    public CutCopyPasteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CutCopyPasteEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean consumed = super.onTextContextMenuItem(id);
        switch (id){
            case android.R.id.cut:
                handleCut();
                break;
            case android.R.id.copy:
                handleCopy();
                break;
            case android.R.id.paste:
                handlePaste();
        }
        return consumed;
    }

    private void handleCut(){
        if(this.cutCopyPasteListener != null)
            this.cutCopyPasteListener.onCut(this);
    }

    private void handleCopy(){
        if(this.cutCopyPasteListener != null)
            this.cutCopyPasteListener.onCopy(this);
    }

    private void handlePaste(){
        if (this.cutCopyPasteListener != null)
            this.cutCopyPasteListener.onPaste(this);
    }
}