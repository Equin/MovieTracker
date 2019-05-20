package com.example.movietracker.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie.MovieResultEntity;
import com.example.movietracker.data.entity.movie.MoviesEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.net.constant.NetConstant;
import com.example.movietracker.view.custom_view.CustomPressableImageView;
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

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>
implements Filterable {

    private List<MovieResultEntity> movieList;
    private List<MovieResultEntity> movieListFull;
    private GenresEntity genresEntity;
    private View.OnClickListener clickListener;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    private View.OnLongClickListener onImageViewLongClickListener;

    public MovieListAdapter(
            MoviesEntity movieList,
            View.OnClickListener clickListener,
            GenresEntity genresEntity,
            CompoundButton.OnCheckedChangeListener onCheckedChangeListener,
            View.OnLongClickListener onImageViewLongClickListener) {
        this.movieList = new ArrayList<>();
        this.movieList.addAll(movieList.getMovies());
        this.movieListFull = new ArrayList<>(this.movieList);
        this.genresEntity = genresEntity;
        this.clickListener = clickListener;
        this.onCheckedChangeListener = onCheckedChangeListener;
        this.onImageViewLongClickListener = onImageViewLongClickListener;
    }

    @NonNull
    @Override
    public MovieListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        view.setOnClickListener(this.clickListener);

        MovieListViewHolder viewHolder = new MovieListViewHolder(view);
        viewHolder.favoriteToggleButton.setOnCheckedChangeListener(onCheckedChangeListener);
        viewHolder.moviePoster.setOnLongClickListener(this.onImageViewLongClickListener);

        return viewHolder;
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

    @Override
    public Filter getFilter() {
        return movieFilter;
    }

    private Filter movieFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MovieResultEntity> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(movieListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (MovieResultEntity item : movieListFull) {
                    if (item.getMovieTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
          notifyDiffUtilAboutChanges(((List<MovieResultEntity>) results.values));
        }
    };

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        private CustomPressableImageView moviePoster;
        private TextView movieReleaseDate;
        private TextView movieTitle;
        private TextView movieGenres;
        private TextView movieRating;
        private ToggleButton favoriteToggleButton;

        MovieListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.moviePoster = itemView.findViewById(R.id.imageView_moviePoster);
            this.movieReleaseDate = itemView.findViewById(R.id.textView_movieReleaseDate);
            this.movieTitle = itemView.findViewById(R.id.textView_movieTitle);
            this.movieGenres = itemView.findViewById(R.id.textView_MovieGenres);
            this.movieRating = itemView.findViewById(R.id.textView_movieRating);
            this.favoriteToggleButton = itemView.findViewById(R.id.toggleButton_favorite);
        }

        void bindMovieToView(MovieResultEntity movie) {
            this.movieTitle.setText(
                    getAppropriateValue(
                            movie.getMovieTitle()));

            this.movieReleaseDate.setText(
                    getYear(movie.getMovieReleaseDate()));

            this.movieGenres.setText(
                    UtilityHelpers.getPipeDividedGenresFromId(
                            movie.getGenreIds(), genresEntity.getGenres()));

            this.movieRating.setText(String.format(Locale.ENGLISH, "%.1f",
                    movie.getMovieVoteAverage()));

            this.itemView.setTag(R.id.tag_int_movie_id, movie.getMovieId());
            this.favoriteToggleButton.setTag(R.id.tag_movieResultEntity_movie_object, movie);
            this.favoriteToggleButton.setChecked(movie.isFavorite());
            this.moviePoster.setImageSourcePathAndName(movie.getPosterPath(), movie.getMovieTitle());

            Glide
                    .with(this.itemView)
                    .load(NetConstant.IMAGE_BASE_URL + movie.getPosterPath())
                    .centerCrop()
                    .into(this.moviePoster);
        }
    }

    public void reloadMovieListWithAdditionalMovies(MoviesEntity newMovieList) {
        notifyDiffUtilAboutChanges(newMovieList.getMovies());
    }

    private void notifyDiffUtilAboutChanges(List<MovieResultEntity> newMovieList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MovieDiffCallback(this.movieList, newMovieList));
        this.movieList.clear();
        this.movieList.addAll(newMovieList);
        diffResult.dispatchUpdatesTo(this);
    }
}
