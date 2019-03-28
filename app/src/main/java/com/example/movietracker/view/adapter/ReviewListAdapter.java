package com.example.movietracker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewResultEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.MoviesEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewListViewHolder> {

    private List<MovieReviewResultEntity> reviewsEntity;

    public ReviewListAdapter(MovieReviewsEntity reviewsEntity) {
        this.reviewsEntity = reviewsEntity.getReviews();
    }

    @NonNull
    @Override
    public ReviewListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_review_item, parent, false);
        return new ReviewListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewListViewHolder holder, int position) {
        MovieReviewResultEntity review =  this.reviewsEntity.get(position);

        holder.reviewerName.setText(review.getReviewAuthor());
        holder.reviewContent.setText(review.getReviewContent());
    }

    @Override
    public int getItemCount() {
        return this.reviewsEntity.size();
    }

    class ReviewListViewHolder extends RecyclerView.ViewHolder {

        private TextView reviewerName;
        private TextView reviewContent;


        ReviewListViewHolder(@NonNull View itemView) {
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
