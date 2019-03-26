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
import com.example.movietracker.data.entity.MovieCastResultEntity;
import com.example.movietracker.data.entity.MovieCastsEntity;
import com.example.movietracker.data.entity.MovieReviewResultEntity;
import com.example.movietracker.data.entity.MovieReviewsEntity;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.net.constant.NetConstant;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    private MovieReviewsEntity reviewsEntity;
    private Context context;

    public ReviewListAdapter(Context context, MovieReviewsEntity reviewsEntity) {
        this.context = context;
        this.reviewsEntity = reviewsEntity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieReviewResultEntity review =  this.reviewsEntity.getReviews().get(position);

        holder.reviewerName.setText(review.getReviewAuthor());
        holder.reviewContent.setText(review.getReviewContent());
    }

    @Override
    public int getItemCount() {
        return this.reviewsEntity.getReviews().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView reviewerName;
        private TextView reviewContent;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.reviewerName = itemView.findViewById(R.id.textView_reviewerName);
            this.reviewContent = itemView.findViewById(R.id.textView_reviewContent);
        }
    }


    public void updateMovieList(MoviesEntity newMovieList) {
     //   DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MovieDiffCallback(this.castsEntity, newMovieList));
     //   diffResult.dispatchUpdatesTo(this);
    }
}
