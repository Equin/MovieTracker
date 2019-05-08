package com.example.movietracker.view.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.movietracker.view.helper.ImageSaveUtility;

public class CustomImageView extends AppCompatImageView {

    private String imageSourcePath;
    private String imageName;

    public CustomImageView(Context context) {
        super(context);
        initialize(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public void setImageSourcePathAndName(String imageSourcePath, String imageName) {
        this.imageSourcePath = imageSourcePath;
        this.imageName = imageName;
    }

    private void initialize(Context context) {
        this.postOnAnimation(()-> {
            this.setOnLongClickListener(v -> {
                ImageSaveUtility.saveImageToDisk(context, this.imageSourcePath, this.imageName);
                return true;
            });

            //added because OnLongClickListener intercept click on recyclerView listener.
            this.setOnClickListener(v -> {
                ((View)v.getParent().getParent()).performClick(); // may not work if hierarchy is different
            });
        });
    }
}
