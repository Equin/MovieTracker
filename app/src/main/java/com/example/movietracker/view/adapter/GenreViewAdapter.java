package com.example.movietracker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.GenreEntity;
import com.example.movietracker.data.entity.GenresEntity;
import com.example.movietracker.view.custom_view.ToggleButtonsView;
import com.google.common.collect.Lists;

import java.util.List;

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
        List<List<GenreEntity>> f = Lists.partition(list.getGenres(), COUNT_TABLE_RECYCLER_VIEWS);
        holder.toggleButtonsView.createButtons(f.get(position), new onCheckedListener());
    }


    @Override
    public int getItemCount() {
        return Lists.partition(list.getGenres(), 3).size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ToggleButtonsView toggleButtonsView;

        ViewHolder(View itemView) {
            super(itemView);

            this.toggleButtonsView = new ToggleButtonsView(context, itemView);
        };
    }

    public class onCheckedListener implements ToggleButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ((GenreEntity)buttonView.getTag()).setSelected(isChecked);
        }
    }

   /* public void setListeners(RecyclerView.OnScrollListener scrollListener, RecyclerView.OnItemTouchListener touchListener) {
        this.touchListener = touchListener;
        this.scrollListener = scrollListener;
    }*/
}