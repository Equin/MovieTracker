package com.example.movietracker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieEntity;
import com.example.movietracker.data.entity.MovieListEntity;
import com.example.movietracker.data.net.constant.NetConstant;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private MovieListEntity list;
    private Context context;

    public MovieListAdapter(Context context, MovieListEntity list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieEntity movie =  this.list.getMovies().get(position);
        holder.movieTitle.setText(
                getAppropriateValue(
                        movie.getMovieTitle()));

        holder.movieReleaseDate.setText(
                getAppropriateValue(
                        getYear(movie.getMovieReleaseDate())));

        holder.movieGenres.setText(
                getAppropriateValue(
                        movie.getGenreIds().toString()));

        holder.movieRating.setText(
                getAppropriateValue(String.format(Locale.ENGLISH, "%.1f",
                        movie.getMovieVoteAverage())));

        Glide
          .with(this.context)
          .load(NetConstant.IMAGE_BASE_URL +movie.getPosterPath())
          .centerCrop()
          .into(holder.moviePoster);

        holder.movieCardView.setTag(movie.getMovieId());
    }

    private int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    private String getAppropriateValue(Object value) {
        return value == null ? "" : value.toString();
    }

    @Override
    public int getItemCount() {
        return this.list.getMovies().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView moviePoster;
        private TextView movieReleaseDate;
        private TextView movieTitle;
        private TextView movieGenres;
        private TextView movieRating;
        private CardView movieCardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.moviePoster = itemView.findViewById(R.id.imageView_moviePoster);
            this.movieReleaseDate = itemView.findViewById(R.id.textView_movieReleaseDate);
            this.movieTitle = itemView.findViewById(R.id.textView_movieTitle);
            this.movieGenres = itemView.findViewById(R.id.textView_MovieGenres);
            this.movieRating = itemView.findViewById(R.id.textView_movieRating);
            this.movieCardView = itemView.findViewById(R.id.cardView_movieCard);
        }
    }
}
