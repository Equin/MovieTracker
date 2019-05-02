package com.example.movietracker.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieResultEntity;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.net.constant.NetConstant;
import com.example.movietracker.view.diff_utill.MovieDiffCallback;
import com.example.movietracker.view.helper.UtilityHelpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.movietracker.view.helper.UtilityHelpers.getAppropriateValue;
import static com.example.movietracker.view.helper.UtilityHelpers.getYear;

public class SearchResultMovieListAdapter extends RecyclerView.Adapter<SearchResultMovieListAdapter.MovieListViewHolder> {

    private List<MovieResultEntity> movieList;
    private View.OnClickListener clickListener;

    public SearchResultMovieListAdapter(
            MoviesEntity movieList,
            View.OnClickListener clickListener) {
        this.movieList = movieList.getMovies();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MovieListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_search_list_item, parent, false);
        view.setOnClickListener(this.clickListener);

        return  new MovieListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListViewHolder holder, int position) {
        MovieResultEntity movie =  this.movieList.get(position);
        holder.bindMovieToView(movie);
    }

    @Override
    public int getItemCount() {
        return this.movieList.size();
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        private ImageView moviePoster;
        private TextView movieReleaseDate;
        private TextView movieTitle;
        private TextView movieRating;

        MovieListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.moviePoster = itemView.findViewById(R.id.imageView_moviePoster);
            this.movieReleaseDate = itemView.findViewById(R.id.textView_movieReleaseDate);
            this.movieTitle = itemView.findViewById(R.id.textView_movieTitle);
            this.movieRating = itemView.findViewById(R.id.textView_movieRating);
        }

        void bindMovieToView(MovieResultEntity movie) {
            this.movieTitle.setText(
                    getAppropriateValue(
                            movie.getMovieTitle()));

            this.movieReleaseDate.setText(
                    getYear(movie.getMovieReleaseDate()));

            this.movieRating.setText(String.format(Locale.ENGLISH, "%.1f",
                    movie.getMovieVoteAverage()));

            this.itemView.setTag(R.id.tag_int_movie_id, movie.getMovieId());

            Glide
                    .with(this.itemView)
                    .load(NetConstant.IMAGE_BASE_URL + movie.getPosterPath())
                    .centerCrop()
                    .into(this.moviePoster);
        }
    }

    public void reloadWithNewResults(MoviesEntity newMovieList) {
        notifyDiffUtilAboutChanges(newMovieList.getMovies());
    }

    private void notifyDiffUtilAboutChanges(List<MovieResultEntity> newMovieList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MovieDiffCallback(this.movieList, newMovieList));
        this.movieList.clear();
        this.movieList.addAll(newMovieList);
        diffResult.dispatchUpdatesTo(this);
    }
}
