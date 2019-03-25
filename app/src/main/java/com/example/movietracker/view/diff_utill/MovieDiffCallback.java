package com.example.movietracker.view.diff_utill;

import com.example.movietracker.data.entity.MoviesEntity;

import androidx.recyclerview.widget.DiffUtil;

public class MovieDiffCallback extends DiffUtil.Callback {

    private MoviesEntity oldMovies;
    private MoviesEntity newMovies;

    public MovieDiffCallback(MoviesEntity oldMovies, MoviesEntity newMovies) {
        this.oldMovies = oldMovies;
        this.newMovies = newMovies;
    }

    @Override
    public int getOldListSize() {
        return this.oldMovies.getMovies().size();
    }

    @Override
    public int getNewListSize() {
        return this.newMovies.getMovies().size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return this.oldMovies.getMovies().get(oldItemPosition)
                .getMovieId() == this.newMovies.getMovies().get(newItemPosition).getMovieId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return this.oldMovies.getMovies().get(oldItemPosition)
                .equals(this.newMovies.getMovies().get(newItemPosition));
    }
}
