package com.example.movietracker.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.movietracker.view.helper.UtilityHelpers.getAppropriateValue;
import static com.example.movietracker.view.helper.UtilityHelpers.getYear;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {

    private List<MovieResultEntity> movieList;
    private GenresEntity genresEntity;
    private View.OnClickListener clickListener;

    public MovieListAdapter(MoviesEntity movieList, View.OnClickListener clickListener, GenresEntity genresEntity ) {
        this.movieList = new ArrayList<>();
        this.movieList.addAll(movieList.getMovies());
        this.genresEntity = genresEntity;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MovieListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        view.setOnClickListener(this.clickListener);
        return new MovieListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListViewHolder holder, int position) {
        MovieResultEntity movie =  this.movieList.get(position);
        holder.movieTitle.setText(
                getAppropriateValue(
                        movie.getMovieTitle()));

        holder.movieReleaseDate.setText(
                        getYear(movie.getMovieReleaseDate()));

        holder.movieGenres.setText(
                UtilityHelpers.getPipeDividedGenresFromId(
                        movie.getGenreIds(), genresEntity.getGenres()));

        holder.movieRating.setText(String.format(Locale.ENGLISH, "%.1f",
                        movie.getMovieVoteAverage()));

        Glide
          .with(holder.itemView)
          .load(NetConstant.IMAGE_BASE_URL +movie.getPosterPath())
          .centerCrop()
          .into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        return this.movieList.size();
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        private ImageView moviePoster;
        private TextView movieReleaseDate;
        private TextView movieTitle;
        private TextView movieGenres;
        private TextView movieRating;

        MovieListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.moviePoster = itemView.findViewById(R.id.imageView_moviePoster);
            this.movieReleaseDate = itemView.findViewById(R.id.textView_movieReleaseDate);
            this.movieTitle = itemView.findViewById(R.id.textView_movieTitle);
            this.movieGenres = itemView.findViewById(R.id.textView_MovieGenres);
            this.movieRating = itemView.findViewById(R.id.textView_movieRating);
        }
    }

    public void updateMovieList(MoviesEntity newMovieList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MovieDiffCallback(this.movieList, newMovieList.getMovies()));
        this.movieList.clear();
        this.movieList.addAll(newMovieList.getMovies());
        diffResult.dispatchUpdatesTo(this);
    }
}
