package com.example.movietracker.data.entity.entity_mapper;

import com.example.movietracker.data.entity.movie_details.cast.MovieCastResultEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;

import java.util.ArrayList;
import java.util.List;

public class MovieCastsDataMapper extends BaseEntityDataMapper<List<MovieCastResultEntity>, MovieCastsEntity> {

    @Override
    public List<MovieCastResultEntity> transformToList(MovieCastsEntity movieCastsEntity) {
        List<MovieCastResultEntity> movieCastResultEntities = new ArrayList<>();

        for (MovieCastResultEntity movieCastResultEntity : movieCastsEntity.getMovieCasts()) {
            movieCastResultEntities.add(
                    new MovieCastResultEntity(
                            movieCastResultEntity.getCastId(),
                            movieCastResultEntity.getCastName(),
                            movieCastResultEntity.getCastImagePath(),
                            movieCastResultEntity.getCastOrder(),
                            movieCastsEntity.getMovieId()));
        }

        return movieCastResultEntities;
    }

    @Override
    public MovieCastsEntity transformFromList(List<MovieCastResultEntity> movieCastResultEntities) {
        if (movieCastResultEntities == null || movieCastResultEntities.size() == 0) {
            return new MovieCastsEntity(-1, new ArrayList<>());
        }
        return new MovieCastsEntity(movieCastResultEntities.get(0).getMovieId(), movieCastResultEntities);
    }
}
