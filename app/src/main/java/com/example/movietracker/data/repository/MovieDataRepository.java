package com.example.movietracker.data.repository;

import com.example.movietracker.data.database.MoviesDatabase;
import com.example.movietracker.data.database.dao.GenresDao;
import com.example.movietracker.data.database.dao.MovieDao;
import com.example.movietracker.data.database.dao.MovieDetailDao;
import com.example.movietracker.view.model.Filters;
import com.example.movietracker.data.entity.entity_mapper.MovieCastsDataMapper;
import com.example.movietracker.data.entity.entity_mapper.MovieReviewsDataMapper;
import com.example.movietracker.data.entity.entity_mapper.MovieVideosDataMapper;
import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.movie_details.cast.MovieCastsEntity;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.entity.movie_details.review.MovieReviewsEntity;
import com.example.movietracker.data.entity.movie_details.video.MovieVideosEntity;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.data.net.RestClient;
import com.example.movietracker.data.net.api.MovieApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MovieDataRepository implements MovieRepository {

    private static final int MOVIE_PER_PAGE = 20;

    private MovieApi movieApi;
    private GenresDao genresDao;
    private MovieDao movieDao;
    private MovieDetailDao movieDetailDao;

    private MovieVideosDataMapper movieVideosDataMapper;
    private MovieCastsDataMapper movieCastsDataMapper;
    private MovieReviewsDataMapper movieReviewsDataMapper;

    private MovieDataRepository() {}

    private static class SingletonHelper {
        private static final MovieDataRepository INSTANCE = new MovieDataRepository();
    }

    public static MovieDataRepository getInstance(){
        return SingletonHelper.INSTANCE;
    }

    @Override
    public void init(RestClient restClient, MoviesDatabase moviesDatabase) {
        this.movieApi = restClient.getMovieApi();
        this.genresDao = moviesDatabase.genresDao();
        this.movieDao = moviesDatabase.movieDao();
        this.movieDetailDao = moviesDatabase.movieDetailDao();
        this.movieCastsDataMapper = new MovieCastsDataMapper();
        this.movieVideosDataMapper = new MovieVideosDataMapper();
        this.movieReviewsDataMapper = new MovieReviewsDataMapper();
    }

    @Override
    public Single<GenresEntity> getGenres() {
        return this.movieApi.getGenres()
                .doOnSuccess(genresEntity -> this.genresDao.saveGenres(genresEntity.getGenres()))
                .onErrorResumeNext(this.genresDao.getAllGenres().map(GenresEntity::new));
    }

    @Override
    public Single<GenresEntity> getLocalGenres() {
        return this.genresDao.getAllGenres().map(genreEntities -> new GenresEntity())
                .onErrorResumeNext(this.movieApi.getGenres()
                        .doOnSuccess(genresEntity -> this.genresDao.saveGenres(genresEntity.getGenres())));
    }

    @Override
    public Observable<MoviesEntity> getMovies(Filters filters) {
        return this.movieApi.getMovies(
                filters.getCommaSeparatedGenres(),
                filters.getSortBy()
                        .concat(".").concat(
                        filters.getOrder().name().toLowerCase()),
                filters.getPage(),
                filters.isIncludeAdult())
                .doOnNext(moviesEntity -> {
                    this.movieDao.saveMovies(moviesEntity.getMovies());
                    this.movieDao.addMovieGenreRelation(moviesEntity.getMovies());
                })
                .onExceptionResumeNext(
                        this.movieDao.getMovies(
                                filters.getSelectedGenresIds(),
                                MOVIE_PER_PAGE,
                                (filters.getPage()-1)*MOVIE_PER_PAGE
                        ).map(movieResultEntities -> {
                                    int totalPage = Math.round((float)this.movieDao.getTotalResults(
                                            filters.getSelectedGenresIds()) / MOVIE_PER_PAGE);

                                    return new MoviesEntity(
                                            filters.getPage(),
                                            totalPage,
                                            movieResultEntities
                                    );
                                }
                        )//.unsubscribeOn(AndroidSchedulers.mainThread())//.firstOrError().toObservable()
                        // some problems... when getting from Retrofit
                        // its Calling OnComplete, but when from DB its not. SO firstOrError().toObservable() added to call onComplete.
                );
    }

    @Override
    public Observable<MoviesEntity> getMovieListForPages(Filters filters) {
        List<Observable<?>> requests = new ArrayList<>();

        for (int i = 1; i <= filters.getPage(); i++) {
            requests.add(addRequest(filters, i));
        }

        return Observable.zip(requests, (Function<Object[], Object>) objects -> {
            MoviesEntity moviesEntity = new MoviesEntity();

            for(int i=0; i<objects.length; i++) {
                moviesEntity.setPage(((MoviesEntity)objects[i]).getPage());
                moviesEntity.setTotalPages(((MoviesEntity)objects[i]).getTotalPages());

                if( moviesEntity.getMovies().size() == 0
                        || (moviesEntity.getMovies().size() > 0
                            && ((MoviesEntity)objects[i]).getMovies().get(0).getMovieId()
                                != moviesEntity.getMovies().get(0).getMovieId())) {
                    moviesEntity.addMovies(((MoviesEntity)objects[i]).getMovies());
                }
            }

            return moviesEntity;
        }).map(o -> new MoviesEntity(((MoviesEntity)o).getPage(), ((MoviesEntity)o).getTotalPages(), ((MoviesEntity)o).getMovies()));
    }

    public Observable<MoviesEntity> addRequest(Filters filters, int page) {
        return this.movieApi.getMoviesForPages(
                filters.getCommaSeparatedGenres(),
                filters.getSortBy()
                        .concat(".").concat(
                        filters.getOrder().name().toLowerCase()),
                page,
                filters.isIncludeAdult())
                .doOnNext(moviesEntity -> {
                    this.movieDao.saveMovies(moviesEntity.getMovies());
                    this.movieDao.addMovieGenreRelation(moviesEntity.getMovies());
                })
                .onExceptionResumeNext(
                        this.movieDao.getMoviesForPages(
                                filters.getSelectedGenresIds(),
                                filters.getPage() * MOVIE_PER_PAGE
                        )
                                .map(movieResultEntities -> {
                                    int totalPage = Math.round((float)this.movieDao.getTotalResults(
                                            filters.getSelectedGenresIds()) / MOVIE_PER_PAGE);

                                      return  new MoviesEntity(filters.getPage(),
                                                totalPage,
                                                movieResultEntities
                                        );
                                })//.unsubscribeOn(AndroidSchedulers.mainThread()) //.firstOrError().toObservable()
                        // some problems... when getting from Retrofit
                        // its Calling OnComplete, but when from DB its not. SO firstOrError().toObservable() added to call onComplete.
                );
    }

    @Override
    public Observable<MovieDetailsEntity> getMovieDetails(int movieId) {
        return this.movieApi.getMovieDetailsById(movieId)
                .doOnNext(movieDetailsEntity ->
                        movieDetailDao.saveInfo(movieDetailsEntity)
                )
                .onExceptionResumeNext(
                        movieDetailDao.getMovieInfo(movieId).map(movieDetailsEntity -> {
                            List<GenreEntity> genreEntities = movieDetailDao.getGenres(movieId).blockingFirst();
                            movieDetailsEntity.setGenres(genreEntities);
                            return movieDetailsEntity;
                        }));
    }

    @Override
    public Observable<MovieCastsEntity> getMovieCasts(int movieId) {
        return this.movieApi.getMovieCasts(movieId)
                .doOnNext(movieCastsEntity -> this.movieDetailDao.saveCasts(
                        this.movieCastsDataMapper.transformToList(movieCastsEntity))
                ).onExceptionResumeNext(
                        this.movieDetailDao.getCasts(movieId)
                                .map(this.movieCastsDataMapper::transformFromList)
                );
    }

    @Override
    public Observable<MovieVideosEntity> getMovieVideos(int movieId) {
        return this.movieApi.getMovieVideos(movieId)
                .doOnNext(movieVideosEntity -> this.movieDetailDao.saveMovieVideos(
                        this.movieVideosDataMapper.transformToList(movieVideosEntity))
                ).onExceptionResumeNext(
                        this.movieDetailDao.getMovieVideos(movieId)
                                .map(this.movieVideosDataMapper::transformFromList)
                );
    }

    @Override
    public Observable<MovieReviewsEntity> getMovieReviews(int movieId) {
        return this.movieApi.getMovieReviews(movieId)
                .doOnNext(movieReviewsEntity -> this.movieDetailDao.saveMovieReviews(
                        this.movieReviewsDataMapper.transformToList(movieReviewsEntity))
                ).onExceptionResumeNext(
                        this.movieDetailDao.getMovieReviews(movieId)
                                .map(this.movieReviewsDataMapper::transformFromList)
                );
    }
}
