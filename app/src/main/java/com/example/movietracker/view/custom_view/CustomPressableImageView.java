package com.example.movietracker.view.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.movietracker.R;

public class CustomPressableImageView extends AppCompatImageView {

    public CustomPressableImageView(Context context) {
        super(context);
        initialize(context);
    }

    public CustomPressableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CustomPressableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public void setImageSourcePathAndName(String imageSourcePath, String imageName) {
        this.setTag(R.id.tag_string_image_name, imageName);
        this.setTag(R.id.tag_string_image_source_path, imageSourcePath);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        setAlpha(pressed ? 0.5f : 1.0f);
    }

    private void initialize(Context context) {
        this.postOnAnimation(()-> {
            //added because OnLongClickListener intercept click on recyclerView listener.
            this.setOnClickListener(v -> {
                ((View)v.getParent().getParent()).performClick(); // may not work if hierarchy is different
            });
        });
    }
}
