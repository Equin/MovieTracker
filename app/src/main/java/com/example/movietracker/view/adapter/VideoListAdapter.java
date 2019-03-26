package com.example.movietracker.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movietracker.data.entity.MovieVideosEntity;
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
    private Lifecycle lifecycle;
    private static YouTubePlayerFullScreenListener listener;

    public VideoListAdapter(MovieVideosEntity movieVideosEntity, Lifecycle lifecycle, YouTubePlayerFullScreenListener listener) {
        this.movieVideosEntity = movieVideosEntity;
        this.lifecycle = lifecycle;
        VideoListAdapter.listener = listener;
    }

    @NonNull
    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_video_item, parent, false);

      //  lifecycle.addObserver(view.findViewById(R.id.youtube_player_view));

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
/*        viewHolder.cueVideo(
                        movieVideosEntity.getMovieVideoResultEntities().get(position).getVideoKey());*/
        viewHolder.loadThumbai( movieVideosEntity.getMovieVideoResultEntities().get(position).getVideoKey());

        viewHolder.videoName.setText(
                        movieVideosEntity.getMovieVideoResultEntities().get(position).getVideoName());

    }

    @Override
    public int getItemCount() {
        return this.movieVideosEntity.getMovieVideoResultEntities().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private YouTubePlayerView youTubePlayerView;
        private YouTubePlayer youTubePlayer;
        private String currentVideoId;

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

        public void loadThumbai(String videoKey) {
            Glide
                    .with(this.itemView)
                    .load(NetConstant.YOUTUBE_THUMBAI_URL +videoKey + "/mqdefault.jpg")
                    .centerCrop()
                    .into(videoImage);
        }

     /*   void cueVideo(String videoId) {
            currentVideoId = videoId;

            if(youTubePlayer == null)
                return;

            youTubePlayer.cueVideo(videoId, 0);
        }*/
    }
}
