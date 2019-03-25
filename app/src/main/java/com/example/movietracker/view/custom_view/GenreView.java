package com.example.movietracker.view.custom_view;

import android.content.Context;
import android.view.View;
import com.example.movietracker.R;
import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.view.adapter.GenreViewAdapter;


import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GenreView {

    private final Context context;
    private View view;

    public GenreView(Context context) {
        this.context = context;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void renderGenreView(GenresEntity genreList) {

        RecyclerView.LayoutManager rowLayoutManager = new LinearLayoutManager(
                this.context, RecyclerView.VERTICAL, false);

        RecyclerView rowRecyclerView = this.view.findViewById(R.id.recyclerView_content);
        rowRecyclerView.setLayoutManager(rowLayoutManager);

        GenreViewAdapter genreViewAdapter = new GenreViewAdapter(this.context, genreList);
        rowRecyclerView.setAdapter(genreViewAdapter);


    }
}
