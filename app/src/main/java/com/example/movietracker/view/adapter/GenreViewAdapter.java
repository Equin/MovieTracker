package com.example.movietracker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.view.custom_view.ToggleButtonsView;
import com.example.movietracker.view.fragment.MainFragment;
import com.google.common.collect.Lists;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class GenreViewAdapter extends RecyclerView.Adapter<GenreViewAdapter.GenreViewHolder> {

    private static final int COUNT_TABLE_RECYCLER_VIEWS = 3;

    private GenresEntity genresEntity;

    public GenreViewAdapter(GenresEntity genresEntity) {
        this.genresEntity = genresEntity;
    }

    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_row, parent, false);

        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GenreViewHolder holder, int position) {
        List<List<GenreEntity>> genres = Lists.partition(genresEntity.getGenres(), COUNT_TABLE_RECYCLER_VIEWS);
      //  holder.toggleButtonsView.createButtons(genres.get(position), new MainFragment.onCheckedListener());
    }

    @Override
    public int getItemCount() {
        return Lists.partition(genresEntity.getGenres(), 3).size();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {

        private ToggleButtonsView toggleButtonsView;

        GenreViewHolder(View itemView) {
            super(itemView);

            this.toggleButtonsView = new ToggleButtonsView(itemView);
        };
    }


}