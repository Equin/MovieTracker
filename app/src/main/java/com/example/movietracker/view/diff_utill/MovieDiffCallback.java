package com.example.movietracker.view.diff_utill;

import com.example.movietracker.data.entity.MovieResultEntity;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

public class MovieDiffCallback extends DiffUtil.Callback {

    private List<MovieResultEntity> oldMovies;
    private List<MovieResultEntity> newMovies;

    public MovieDiffCallback(List<MovieResultEntity> oldMovies, List<MovieResultEntity> newMovies) {
        this.oldMovies = oldMovies;
        this.newMovies = newMovies;
    }

    @Override
    public int getOldListSize() {
        return this.oldMovies.size();
    }

    @Override
    public int getNewListSize() {
        return this.newMovies.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return this.oldMovies.get(oldItemPosition)
                .getMovieId() == this.newMovies.get(newItemPosition).getMovieId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return this.oldMovies.get(oldItemPosition)
                .equals(this.newMovies.get(newItemPosition));
    }
}
