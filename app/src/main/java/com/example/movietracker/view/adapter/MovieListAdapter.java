package com.example.movietracker.view.adapter;

import android.content.Context;
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
import com.example.movietracker.di.DataProvider;
import com.example.movietracker.view.diff_utill.MovieDiffCallback;
import com.example.movietracker.view.helper.UtilityHelpers;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.movietracker.view.helper.UtilityHelpers.getAppropriateValue;
import static com.example.movietracker.view.helper.UtilityHelpers.getYear;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private MoviesEntity movieList;
    private GenresEntity genresEntity;
    private Context context;
    private View.OnClickListener clickListener;

    public MovieListAdapter(Context context, MoviesEntity movieList) {
        this.context = context;
        this.movieList = movieList;
        this.genresEntity = DataProvider.genresEntity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieResultEntity movie =  this.movieList.getMovies().get(position);
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

        holder.movieCardView.setTag(movie.getMovieId());
        holder.movieCardView.setOnClickListener(this.clickListener);
    }

    @Override
    public int getItemCount() {
        return this.movieList.getMovies().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView moviePoster;
        private TextView movieReleaseDate;
        private TextView movieTitle;
        private TextView movieGenres;
        private TextView movieRating;
        private CardView movieCardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.moviePoster = itemView.findViewById(R.id.imageView_moviePoster);
            this.movieReleaseDate = itemView.findViewById(R.id.textView_movieReleaseDate);
            this.movieTitle = itemView.findViewById(R.id.textView_movieTitle);
            this.movieGenres = itemView.findViewById(R.id.textView_MovieGenres);
            this.movieRating = itemView.findViewById(R.id.textView_movieRating);
            this.movieCardView = itemView.findViewById(R.id.cardView_movieCard);
        }
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void updateMovieList(MoviesEntity newMovieList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MovieDiffCallback(this.movieList, newMovieList));
        diffResult.dispatchUpdatesTo(this);
    }
}
