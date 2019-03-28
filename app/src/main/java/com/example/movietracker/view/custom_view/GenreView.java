package com.example.movietracker.view.custom_view;

import android.content.Context;
import android.view.View;
import com.example.movietracker.R;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.view.adapter.GenreViewAdapter;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GenreView {

    private final Context context;
    private final RecyclerView recyclerView;
    private View view;

    public GenreView(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    public void renderGenreView(GenresEntity genreList) {

        RecyclerView.LayoutManager rowLayoutManager = new LinearLayoutManager(
                this.context, RecyclerView.VERTICAL, false);

        this.recyclerView.setLayoutManager(rowLayoutManager);

        GenreViewAdapter genreViewAdapter = new GenreViewAdapter(genreList);
        this.recyclerView.setAdapter(genreViewAdapter);
    }
}
