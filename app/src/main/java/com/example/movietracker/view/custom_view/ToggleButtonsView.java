package com.example.movietracker.view.custom_view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;

import java.util.List;

public class ToggleButtonsView  {

    private static final int TOGGLE_BUTTON_ID_PREFIX = 300;
    private final View view;

    public ToggleButtonsView(View view) {
        this.view = view;
    }

    public void createButtons(List<GenreEntity> genres, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {

        LinearLayout linearLayout = new LinearLayout(view.getContext());

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        linearLayoutParams.setMargins(5,5,5,5);

         for(int i = 0; i<genres.size(); i++) {

             String title = genres.get(i).getGenreName();

             ToggleButton toggleButton = new ToggleButton(this.view.getContext());
             toggleButton.setId(i+TOGGLE_BUTTON_ID_PREFIX);
             toggleButton.setTag(genres.get(i));

             toggleButton.setText(title);
             toggleButton.setTextOff(title);
             toggleButton.setTextOn(title);

             toggleButton.setSelected(genres.get(i).isSelected());

             toggleButton.setPadding(25,0,25,0);
             toggleButton.setBackgroundDrawable(this.view.getContext().getDrawable(R.drawable.main_button_background));
             toggleButton.setLayoutParams(linearLayoutParams);

             toggleButton.setOnCheckedChangeListener(onCheckedChangeListener);
             linearLayout.addView(toggleButton);
         }
    }
}
