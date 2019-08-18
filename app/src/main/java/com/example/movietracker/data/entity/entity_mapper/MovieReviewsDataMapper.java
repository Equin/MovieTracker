package com.example.movietracker.data.entity.entity_mapper;

import com.example.movietracker.data.entity.movie_details.review.MovieReviewResultEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideoResultEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewsDataMapper extends BaseEntityDataMapper<List<MovieReviewResultEntity>, MovieReviewsEntity> {

    @Override
    public List<MovieReviewResultEntity> transformToList(MovieReviewsEntity movieReviewsEntity) {
        List<MovieReviewResultEntity> movieReviewResultEntities = new ArrayList<>();

        for (MovieReviewResultEntity movieReviewResultEntity : movieReviewsEntity.getReviews()) {
            movieReviewResultEntities.add(
                    new MovieReviewResultEntity(
                            movieReviewResultEntity.getReviewId(),
                            movieReviewResultEntity.getReviewAuthor(),
                            movieReviewResultEntity.getReviewContent(),
                            movieReviewResultEntity.getReviewUrl(),
                            movieReviewsEntity.getMovieId()));
        }

        return movieReviewResultEntities;
    }

    @Override
    public MovieReviewsEntity transformFromList(List<MovieReviewResultEntity> movieReviewResultEntities) {
        if (movieReviewResultEntities.isEmpty()) {
            return new MovieReviewsEntity(0, new ArrayList<>());
        }
        return new MovieReviewsEntity(movieReviewResultEntities.get(0).getMovieId(), movieReviewResultEntities);
    }
}
