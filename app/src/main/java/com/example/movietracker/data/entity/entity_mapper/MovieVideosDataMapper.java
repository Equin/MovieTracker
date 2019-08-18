package com.example.movietracker.data.entity.entity_mapper;

import com.example.movietracker.data.entity.movie_details.cast.MovieCastResultEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideoResultEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;

import java.util.ArrayList;
import java.util.List;

public class MovieVideosDataMapper extends BaseEntityDataMapper<List<MovieVideoResultEntity>, MovieVideosEntity> {

    @Override
    public List<MovieVideoResultEntity> transformToList(MovieVideosEntity movieVideosEntity) {
        List<MovieVideoResultEntity> movieVideoResultEntities = new ArrayList<>();

        for (MovieVideoResultEntity movieVideoResultEntity : movieVideosEntity.getMovieVideoResultEntities()) {
            movieVideoResultEntities.add(
                    new MovieVideoResultEntity(
                            movieVideoResultEntity.getVideoId(),
                            movieVideoResultEntity.getIso639(),
                            movieVideoResultEntity.getIso3166(),
                            movieVideoResultEntity.getVideoKey(),
                            movieVideoResultEntity.getVideoName(),
                            movieVideoResultEntity.getVideoSite(),
                            movieVideoResultEntity.getVideoSize(),
                            movieVideoResultEntity.getVideoType(),
                            movieVideosEntity.getMovieId()));
        }

        return movieVideoResultEntities;
    }

    @Override
    public MovieVideosEntity transformFromList(List<MovieVideoResultEntity> movieVideoResultEntity) {
        if (movieVideoResultEntity.isEmpty()) {
            return new MovieVideosEntity(0, new ArrayList<>());
        }
        return new MovieVideosEntity(movieVideoResultEntity.get(0).getMovieId(), movieVideoResultEntity);
    }
}
