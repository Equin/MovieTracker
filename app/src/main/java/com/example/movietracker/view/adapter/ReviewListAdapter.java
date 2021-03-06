package com.example.movietracker.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewResultEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;

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
        holder.bindMovieReview(this.reviewsEntity.get(position));
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

        void bindMovieReview(MovieReviewResultEntity review) {
            this.reviewerName.setText(review.getReviewAuthor());
            this.reviewContent.setText(review.getReviewContent());
        }
    }
}
