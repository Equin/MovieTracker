package com.example.movietracker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.GenreEntity;
import com.example.movietracker.data.entity.GenresEntity;
import com.google.common.collect.Lists;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GenreViewAdapter extends RecyclerView.Adapter<GenreViewAdapter.ViewHolder> {

    private static final int COUNT_TABLE_RECYCLER_VIEWS = 3;

    private GenresEntity list;
    private Context context;

    public GenreViewAdapter(Context context, GenresEntity teams) {
        this.context = context;
        this.list = teams;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.rowRecycleView.setLayoutManager(holder.layoutManager);

        List<List<GenreEntity>> f = Lists.partition(list.getGenres(), COUNT_TABLE_RECYCLER_VIEWS);

        GenreRowAdapter genreRowAdapter = new GenreRowAdapter(context, f.get(position));
        holder.rowRecycleView.setAdapter(genreRowAdapter);
    }


    @Override
    public int getItemCount() {
        return Lists.partition(list.getGenres(), 3).size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView rowRecycleView;
        private RecyclerView.LayoutManager layoutManager;

        ViewHolder(View itemView) {
            super(itemView);

            this.rowRecycleView = itemView.findViewById(R.id.recyclerView_row);
            this.layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        };
    }

   /* public void setListeners(RecyclerView.OnScrollListener scrollListener, RecyclerView.OnItemTouchListener touchListener) {
        this.touchListener = touchListener;
        this.scrollListener = scrollListener;
    }*/
}