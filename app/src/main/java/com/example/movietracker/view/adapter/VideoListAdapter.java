package com.example.movietracker.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.data.net.constant.NetConstant;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movietracker.R;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private MovieVideosEntity movieVideosEntity;
    private View.OnClickListener clickListener;

    public VideoListAdapter(MovieVideosEntity movieVideosEntity) {
        this.movieVideosEntity = movieVideosEntity;
    }

    @NonNull
    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String videoId = movieVideosEntity.getMovieVideoResultEntities().get(position).getVideoKey();

        viewHolder.loadThumbai(videoId);
        viewHolder.videoName.setText(
                        movieVideosEntity.getMovieVideoResultEntities().get(position).getVideoName());

        viewHolder.itemView.setOnClickListener(this.clickListener);
        viewHolder.itemView.setTag(videoId);
    }

    @Override
    public int getItemCount() {
        return this.movieVideosEntity.getMovieVideoResultEntities().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView videoName;
        private ImageView videoImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoImage = itemView.findViewById(R.id.imageView_videoThumbai);
            videoName = itemView.findViewById(R.id.textView_videoName);

         /*   youTubePlayerView.addFullScreenListener(listener);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState state) {
                    super.onStateChange(youTubePlayer, state);
                    if (state == PlayerConstants.PlayerState.PLAYING) {
                        youTubePlayerView.getPlayerUiController().showCurrentTime(true);
                        youTubePlayerView.getPlayerUiController().showDuration(true);
                        youTubePlayerView.getPlayerUiController().showFullscreenButton(true);
                        youTubePlayerView.getPlayerUiController().showSeekBar(true);
                        youTubePlayerView.getPlayerUiController().showUi(true);
                    }
                }

                @Override
                public void onReady(@NonNull YouTubePlayer initializedYouTubePlayer) {
                    youTubePlayer = initializedYouTubePlayer;
                    youTubePlayer.cueVideo(currentVideoId, 0);
                }
            });*/
        }

        void loadThumbai(String videoKey) {
            Glide
                    .with(this.itemView)
                    .load(NetConstant.YOUTUBE_THUMBAI_URL +videoKey + "/mqdefault.jpg")
                    .centerCrop()
                    .into(videoImage);
        }
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
