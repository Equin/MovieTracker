package com.example.movietracker.data.repository;

import com.example.movietracker.data.database.MoviesDatabase;
import com.example.movietracker.data.database.dao.GenresDao;
import com.example.movietracker.data.database.dao.MovieDao;
import com.example.movietracker.data.database.dao.MovieDetailDao;
import com.example.movietracker.data.entity.movie.MovieChangesEntity;
import com.example.movietracker.data.entity.movie.MovieResultEntity;
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
import com.example.movietracker.data.entity.movie.MoviesEntity;
import com.example.movietracker.data.net.RestClient;
import com.example.movietracker.data.net.api.MovieApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MovieDataRepository implements MovieRepository {

    private static final int MOVIE_PER_PAGE = 20;


    private MovieApi movieApi;
    private GenresDao genresDao;
    private MovieDao movieDao;
    private MovieDetailDao movieDetailDao;

    private MovieVideosDataMapper movieVideosDataMapper;
    private MovieCastsDataMapper movieCastsDataMapper;
    private MovieReviewsDataMapper movieReviewsDataMapper;

   private UserRepository userRepository;

    private MovieDataRepository() {}

    private static class SingletonHelper {
        private static final MovieDataRepository INSTANCE = new MovieDataRepository();
    }

    public static MovieDataRepository getInstance(){
        return SingletonHelper.INSTANCE;
    }

    @Override
    public void init(RestClient restClient, MoviesDatabase moviesDatabase, UserRepository userRepository) {
        this.movieApi = restClient.getMovieApi();
        this.genresDao = moviesDatabase.getGenresDao();
        this.movieDao = moviesDatabase.getMovieDao();
        this.movieDetailDao = moviesDatabase.getMovieDetailDao();
        this.movieCastsDataMapper = new MovieCastsDataMapper();
        this.movieVideosDataMapper = new MovieVideosDataMapper();
        this.movieReviewsDataMapper = new MovieReviewsDataMapper();
        this.userRepository = userRepository;
    }

    /**
     * getting genres from server and saving to db, if error -> gets from db
     * @return Single<GenresEntity>
     */
    @Override
    public Single<GenresEntity> getGenres() {
        return this.movieApi.getGenres()
                .doOnSuccess(genresEntity -> this.genresDao.saveGenres(genresEntity.getGenres()))
                .onErrorResumeNext(this.genresDao.getAllGenres().map(GenresEntity::new));
    }

    /**
     * getting genres from DB, if error -> from server and saving to db
     * @return Single<GenresEntity>
     */
    @Override
    public Single<GenresEntity> getLocalGenres() {
        return this.genresDao.getAllGenres().map(genreEntities -> new GenresEntity())
                .onErrorResumeNext(this.movieApi.getGenres()
                        .doOnSuccess(genresEntity -> this.genresDao.saveGenres(genresEntity.getGenres())));
    }

    /**
     * getting movies by filters from server and saving to db, if error -> getting from Db
     * @param filters
     * @return Observable<MoviesEntity>
     */
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
                                (filters.getPage()-1)*MOVIE_PER_PAGE,
                                filters.isIncludeAdult()
                        )
                                .map(movieResultEntities -> {
                                    int totalPage = Math.round((float)this.movieDao.getTotalResults(
                                            filters.getSelectedGenresIds(),
                                            filters.isIncludeAdult()) / MOVIE_PER_PAGE);

                                    return new MoviesEntity(
                                            filters.getPage(),
                                            totalPage,
                                            movieResultEntities
                                    );
                                }
                        )
                );
    }

    /**
     * getting movies by title and saving it to db, if there is error it searching movies in db
     * @param filters
     * @return Observable<MoviesEntity>
     */
    @Override
    public Observable<MoviesEntity> getMoviesByTitle(Filters filters) {
        return this.movieApi.getMoviesByTitle(filters.getSearchQueryByTitle(), filters.isIncludeAdult())
                .doOnNext(moviesEntity -> {
                    this.movieDao.saveMovies(moviesEntity.getMovies());
                    this.movieDao.addMovieGenreRelation(moviesEntity.getMovies());
                })
                .onExceptionResumeNext(
                        this.movieDao.getMovieByTitle(filters.getSearchQueryByTitle(), filters.isIncludeAdult()).map(movieResultEntities ->
                                new MoviesEntity(1,1, movieResultEntities)));
    }

    /**
     * getting from server movies ids that has changes for past 24h
     * @return Observable<MovieChangesEntity>
     */
    @Override
    public Observable<MovieChangesEntity> getMoviesChanges() {
        return this.movieApi.getMoviesChanges(1).flatMap(movieChangesEntity -> {
            List<Observable<MovieChangesEntity>> requests = new ArrayList<>();
           if (movieChangesEntity.getTotalPages() > 1) {
               for (int i = 1; i <movieChangesEntity.getTotalPages(); i++) {
                   requests.add(addMovieChangesRequest(i));
               }
           } else {
              return Observable.just(movieChangesEntity);
           }
           return Observable.zip(requests, (moviesChangesList) -> {
               MovieChangesEntity moviesChangesNewList = new MovieChangesEntity();

               for (Object moviesChanges : moviesChangesList) {
                   MovieChangesEntity movieChangesCast = (MovieChangesEntity) moviesChanges;

                   moviesChangesNewList.addMovieChanges(movieChangesCast.getResults());
                   moviesChangesNewList.setPage(movieChangesCast.getPage());
                   moviesChangesNewList.setTotalPages(movieChangesCast.getTotalPages());
               }

               return moviesChangesNewList;
           });
        });
    }

    private Observable<MovieChangesEntity> addMovieChangesRequest(int page) {
        return this.movieApi.getMoviesChanges(page);
    }

    @Override
    public Observable<MoviesEntity> getMoviesWithFavorites(Filters filters) {
        return  getMovieFavorites(getMovies(filters));
    }

    /**
     * getting movies with favorites by zipping getUserWithFavorites with moviesEntityObservable that has movieListForPages
     *
     *  checking movies from userWithFavorites and moviesEntityObservable, and there is a match its mark movie as favorite
     * @param moviesEntityObservable - movie list for pages
     * @return Observable<MoviesEntity>
     */
    private Observable<MoviesEntity> getMovieFavorites( Observable<MoviesEntity> moviesEntityObservable ) {
        return Observable.zip(
                this.userRepository.getUserWithFavorites().subscribeOn(Schedulers.newThread()),
                moviesEntityObservable.subscribeOn(Schedulers.newThread()),
                (userEntity, moviesEntity) -> {

                    if (userEntity.getFavoriteMovies() == null || userEntity.getFavoriteMovies().isEmpty()) {
                        return moviesEntity;
                    }

                    for (MovieResultEntity movieResultEntity : moviesEntity.getMovies()) {
                        boolean isFavorite = false;
                        for (MovieResultEntity userFavMovies : userEntity.getFavoriteMovies()) {
                            if(movieResultEntity.getMovieId() == userFavMovies.getMovieId()) {
                                isFavorite = true;
                            }
                        }
                        movieResultEntity.setFavorite(isFavorite);
                    }
                    return moviesEntity;
                });
    }

    @Override
    public Observable<MoviesEntity> getMovieListForPagesWithFavorites(Filters filters) {
       return  getMovieFavorites(getMovieListForPages(filters));
    }

    /**
     * getting movies for each page, for ading it to one list.
     * @param filters
     * @return Observable<MoviesEntity>
     */
    @Override
    public Observable<MoviesEntity> getMovieListForPages(Filters filters) {
        List<Observable<MoviesEntity>> requests = new ArrayList<>();

        for (int i = 1; i <= filters.getPage(); i++) {
            requests.add(addRequest(filters, i));
        }

        return Observable.zip(requests, movies -> {
            MoviesEntity moviesEntity = new MoviesEntity();

            for (Object movie : movies) {
                moviesEntity.setPage(((MoviesEntity) movie).getPage());
                moviesEntity.setTotalPages(((MoviesEntity) movie).getTotalPages());

                //removing duplicates of movie pages
                if (moviesEntity.getMovies().isEmpty()
                        || (!moviesEntity.getMovies().isEmpty()
                        && ((MoviesEntity) movie).getMovies().get(0).getMovieId()
                        != moviesEntity.getMovies().get(0).getMovieId())) {
                    moviesEntity.addMovies(((MoviesEntity) movie).getMovies());
                }
            }

            return moviesEntity;
        });
    }

    /**
     * adding requests for getting movies from server (and saving it to db) for each page in one list.
     * if there is errror it gets movies form db
     * @param filters
     * @param page
     * @return Observable<MoviesEntity>
     */
    private Observable<MoviesEntity> addRequest(Filters filters, int page) {
        return this.movieApi.getMovies(
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
                        this.movieDao.getMovies(
                                filters.getSelectedGenresIds(),
                                filters.getPage() * MOVIE_PER_PAGE,
                                0,
                                filters.isIncludeAdult()
                        )
                                .map(movieResultEntities -> {
                                    int totalPage = Math.round((float)this.movieDao.getTotalResults(
                                            filters.getSelectedGenresIds(),
                                            filters.isIncludeAdult()) / MOVIE_PER_PAGE);

                                      return new MoviesEntity(
                                              filters.getPage(),
                                                totalPage,
                                                movieResultEntities
                                        );
                                })
                );
    }


    /**
     * getting movie details from server and saving it,  if there is error it gets details from DB
     * @param movieId
     * @return Observable<MovieDetailsEntity>
     */
    @Override
    public Observable<MovieDetailsEntity> getMovieDetails(int movieId) {
        return this.movieApi.getMovieDetailsById(movieId)
                .doOnNext(movieDetailsEntity ->
                        movieDetailDao.saveInfo(movieDetailsEntity)
                )
                .onExceptionResumeNext(
                        movieDetailDao.getMovieInfo(movieId)
                                .map(movieDetailsEntity -> {
                                    if (!movieDetailsEntity.isEmpty()) {
                                        List<GenreEntity> genreEntities = movieDetailDao.getGenres(movieId).blockingFirst();
                                        movieDetailsEntity.get(0).setGenres(genreEntities);
                                        return movieDetailsEntity.get(0);
                                    }
                                    else  {
                                        return new MovieDetailsEntity();
                                    }
                        }));
    }

    /**
     * getting movie casts from server and saving it,  if there is error it gets casts from DB
     * @param movieId
     * @return Observable<MovieCastsEntity>
     */
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

    /**
     * getting movie videos from server and saving it,  if there is error it gets videos from DB
     * @param movieId
     * @return Observable<MovieVideosEntity>
     */
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

    /**
     * getting movie reviews from server and saving it,  if there is error it gets reviews from DB
     * @param movieId
     * @return Observable<MovieReviewsEntity>
     */
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

    @Override
    public Observable<List<Integer>> getMoviesIdList() {
        return this.movieDao.getMoviesIdList();
    }
}
