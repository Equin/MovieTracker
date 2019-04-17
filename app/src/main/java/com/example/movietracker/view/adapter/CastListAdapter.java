package com.example.movietracker.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.movietracker.AndroidApplication;
import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastResultEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.net.constant.NetConstant;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CastListAdapter extends RecyclerView.Adapter<CastListAdapter.CastListViewHolder> {

    private List<MovieCastResultEntity> castResultEntity;

    public CastListAdapter(MovieCastsEntity castsEntity) {
        this.castResultEntity = castsEntity.getMovieCasts();
    }

    @NonNull
    @Override
    public CastListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_cast_item, parent, false);
        return new CastListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastListViewHolder holder, int position) {
        holder.bindMovieCastToView(this.castResultEntity.get(position));
    }

    @Override
    public int getItemCount() {
        return this.castResultEntity.size();
    }

    class CastListViewHolder extends RecyclerView.ViewHolder {

        private ImageView castPhoto;
        private TextView castName;

        CastListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.castPhoto = itemView.findViewById(R.id.imageView_castPhoto);
            this.castName = itemView.findViewById(R.id.textView_castName);
        }

        void bindMovieCastToView(MovieCastResultEntity cast) {
            this.castName.setText(cast.getCastName());

            Glide
                    .with(AndroidApplication.getRunningActivity().getApplicationContext())
                    .load(NetConstant.IMAGE_BASE_URL +cast.getCastImagePath())
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this.castPhoto);
        }
    }
}
