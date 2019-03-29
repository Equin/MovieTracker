package com.example.movietracker.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movietracker.data.entity.movie_details.video.MovieVideoResultEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.data.net.constant.NetConstant;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movietracker.R;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder> {

    private List<MovieVideoResultEntity> movieVideoResultEntity;
    private View.OnClickListener clickListener;

    public VideoListAdapter(MovieVideosEntity movieVideosEntity, View.OnClickListener clickListener) {
        this.movieVideoResultEntity = movieVideosEntity.getMovieVideoResultEntities();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public VideoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_video_item, parent, false);
        view.setOnClickListener(this.clickListener);
        return new VideoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListViewHolder viewHolder, int position) {
        String videoId = movieVideoResultEntity.get(position).getVideoKey();

        viewHolder.loadThumbai(videoId);
        viewHolder.videoName.setText(
                movieVideoResultEntity.get(position).getVideoName());
    }

    @Override
    public int getItemCount() {
        return this.movieVideoResultEntity.size();
    }

    static class VideoListViewHolder extends RecyclerView.ViewHolder {

        private TextView videoName;
        private ImageView videoImage;

        VideoListViewHolder(@NonNull View itemView) {
            super(itemView);
            videoImage = itemView.findViewById(R.id.imageView_videoThumbai);
            videoName = itemView.findViewById(R.id.textView_videoName);
        }

        void loadThumbai(String videoKey) {
            Glide
                    .with(this.itemView)
                    .load(NetConstant.YOUTUBE_THUMBAI_URL
                            + videoKey
                            + NetConstant.YOUTUBE_THUMBAI_IMAGE_SIZE_URL_SUFIX)
                    .centerCrop()
                    .into(videoImage);
        }
    }
}
