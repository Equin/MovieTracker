package com.example.movietracker.presenter;

import com.example.movietracker.R;
import com.example.movietracker.data.entity.MovieRequestEntity;
import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.data.entity.MoviesEntity;
import com.example.movietracker.interactor.DefaultObserver;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.model.model_impl.MovieModelImpl;
import com.example.movietracker.view.contract.MovieListView;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class MovieListPresenter extends BasePresenter {

    private MovieListView view;
    private String genresIdToLoadMoviesBy;
    //private MoviesEntity moviesEntity;
  //  private List<MovieResultEntity> movieResultEntities;
    private MovieRequestEntity movieRequestEntity;
    private ModelContract.MovieModel movieModel;

    public MovieListPresenter(MovieListView view) {
        this.view = view;
        this.movieModel = new MovieModelImpl();
        initialize();
    }

    private void initialize() {
        //this.moviesEntity = new MoviesEntity();
      //  this.movieResultEntities = new ArrayList<>();
        this.movieRequestEntity = new MovieRequestEntity();
        showLoading();
    }

    public void onMovieItemClicked(int clickedMovieId) {
        this.view.showMovieDetailScreen(clickedMovieId);
    }

    public void getMoviesByGenres(GenresEntity genresEntity) {
        List<GenreEntity> genreEntity = genresEntity.getGenres();
        StringBuilder sb = new StringBuilder();

        for(GenreEntity genre : genreEntity) {
            sb.append(genre.getGenreId()).append(",");
        }

        this.genresIdToLoadMoviesBy = sb.toString();
        this.movieRequestEntity.setPage(1);
        this.movieRequestEntity.setGenresId(this.genresIdToLoadMoviesBy);

        getMovies(this.movieRequestEntity);
    }



    public void getMoviesByFilters(MovieRequestEntity movieRequestEntity) {


    }

    private void getMovies(MovieRequestEntity movieRequestEntity) {
        this.movieModel.getMovies(new GetMoviesObserver(), movieRequestEntity);
    }

    private void showLoading() {
        this.view.showLoading();
    }

    private void hideLoading() {
        this.view.hideLoading();
    }


    @Override
    public void destroy() {
        this.movieModel.stop();
        this.view = null;
    }


    private class GetMoviesObserver extends DefaultObserver<MoviesEntity> {
        @Override
        public void onNext(MoviesEntity moviesEntity) {
            MovieListPresenter.this.view.renderMoviesList(moviesEntity);
            MovieListPresenter.this.hideLoading();
        }

        @Override
        public void onError(@NonNull Throwable e) {
            MovieListPresenter.this.view.showToast(R.string.main_error);
            MovieListPresenter.this.hideLoading();
        }
    }

}

