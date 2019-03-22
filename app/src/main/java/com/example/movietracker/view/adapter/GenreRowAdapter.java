package com.example.movietracker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.GenreEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GenreRowAdapter  extends RecyclerView.Adapter<GenreRowAdapter.ViewHolder> {

    private  List<GenreEntity> list;
    private Context context;

    public GenreRowAdapter(Context context, List<GenreEntity> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.button_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = this.list.get(position).getGenreName();
        holder.toggleButton.setText(title);
        holder.toggleButton.setTextOff(title);
        holder.toggleButton.setTextOn(title);
        holder.toggleButton.setTag(this.list.get(position).getGenreId());
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ToggleButton toggleButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.toggleButton = itemView.findViewById(R.id.toggleButton);
        }
    }
}
