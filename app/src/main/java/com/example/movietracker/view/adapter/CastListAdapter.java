package com.example.movietracker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastResultEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.net.constant.NetConstant;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CastListAdapter extends RecyclerView.Adapter<CastListAdapter.ViewHolder> {

    private MovieCastsEntity castsEntity;
    private Context context;

    public CastListAdapter(Context context, MovieCastsEntity castsEntity) {
        this.context = context;
        this.castsEntity = castsEntity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_cast_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieCastResultEntity cast =  this.castsEntity.getMovieCasts().get(position);

        holder.castName.setText(cast.getCastName());

        Glide
          .with(this.context)
          .load(NetConstant.IMAGE_BASE_URL +cast.getCastImagePath())
          .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
          .into(holder.castPhoto);
    }


    private String getAppropriateValue(Object value) {
        return value == null ? "" : value.toString();
    }

    @Override
    public int getItemCount() {
        return this.castsEntity.getMovieCasts().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView castPhoto;
        private TextView castName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.castPhoto = itemView.findViewById(R.id.imageView_castPhoto);
            this.castName = itemView.findViewById(R.id.textView_castName);
        }
    }


    public void updateMovieList(MoviesEntity newMovieList) {
     //   DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MovieDiffCallback(this.castsEntity, newMovieList));
     //   diffResult.dispatchUpdatesTo(this);
    }
}
