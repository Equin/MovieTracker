package com.example.movietracker.feature.search_view;

import android.util.Log;
import android.view.View;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movietracker.data.entity.movie.MovieResultEntity;
import com.example.movietracker.data.entity.movie.MoviesEntity;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.presenter.MainPresenter;
import com.example.movietracker.view.adapter.SearchResultMovieListAdapter;
import com.example.movietracker.view.custom_view.CustomToolbarSearchView;
import com.example.movietracker.view.helper.RxDisposeHelper;
import com.example.movietracker.view.model.Filters;
import com.jakewharton.rxbinding3.appcompat.RxSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchFeature {

    private static final String TAG = MainPresenter.class.getCanonicalName();

    private RecyclerView.OnClickListener recyclerViewItemClickListener;
    private SearchResultMovieListAdapter searchResultMovieListAdapter;
    private CustomToolbarSearchView customToolbarSearchView;
    private ModelContract.MovieModel movieModel;
    private Filters filters;
    private MutableLiveData<MoviesEntity>  moviesEntityLiveData;
    private Disposable disposable;

    private SearchFeature() {}

    private static class SingletonHelper {
        private static final SearchFeature INSTANCE = new SearchFeature();
    }

    public static SearchFeature getInstance(){
        return SingletonHelper.INSTANCE;
    }

    public void init(CustomToolbarSearchView customToolbarSearchView, ModelContract.MovieModel movieModel, RecyclerView.OnClickListener recyclerViewItemClickListener) {
        this.movieModel = movieModel;
        this.customToolbarSearchView = customToolbarSearchView;
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
        this.filters = Filters.getInstance();
        setupSearchView();
    }

    private MutableLiveData<MoviesEntity> getMutableLiveData() {
        if (moviesEntityLiveData == null) {
            moviesEntityLiveData = new MutableLiveData<MoviesEntity>();
        }
        return moviesEntityLiveData;
    }


    private void setupSearchView() {
        this.searchResultMovieListAdapter
                = new SearchResultMovieListAdapter(new MoviesEntity(), recyclerViewItemClickListener);
        this.customToolbarSearchView.setRecyclerViewAdapter(this.searchResultMovieListAdapter);

      disposable = RxSearchView.queryTextChanges(this.customToolbarSearchView)
              .filter(charSequence -> charSequence.length() > 1)
                .observeOn(Schedulers.io())
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap( searchQuery ->
                    {
                        filters.setSearchQueryByTitle(searchQuery.toString());

                        MoviesEntity searchHistory = getSearchHistoryResults(searchQuery);

                        if (!searchHistory.getMovies().isEmpty()) {
                            return Observable.just(searchHistory);
                        } else {
                            return getResultFormServer();
                        }
                    }
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GetMovieByTitleObserver());
    }

    private Observable<MoviesEntity> getResultFormServer() {
        return  movieModel.getMoviesByTitle(filters).doOnNext(moviesEntity ->
        {
            if (getMutableLiveData().getValue() == null) {
                getMutableLiveData().postValue(moviesEntity);
            } else {
                MoviesEntity mutableMovieValueTemp = getMutableLiveData().getValue();
                List<MovieResultEntity> movieResultListTemp = mutableMovieValueTemp.getMovies();

                movieResultListTemp.addAll(moviesEntity.getMovies());
                mutableMovieValueTemp.setMovies(movieResultListTemp);
                getMutableLiveData().postValue(mutableMovieValueTemp);
            }
        });
    }

    private MoviesEntity getSearchHistoryResults(CharSequence s) {
        MoviesEntity moviesLocal = new MoviesEntity();
        List<MovieResultEntity> movieResultEntitiesLocal = new ArrayList<>();

        if ( getMutableLiveData().getValue() != null) {
            for (MovieResultEntity movieResultEntity : getMutableLiveData().getValue().getMovies()) {
                if (movieResultEntity.getMovieTitle().toLowerCase().contains(s)) {
                    if(!movieResultEntitiesLocal.contains(movieResultEntity)) {
                        movieResultEntitiesLocal.add(movieResultEntity);
                    }
                    moviesLocal.setMovies(movieResultEntitiesLocal);
                    if (movieResultEntity.isAdult() == !filters.isIncludeAdult()) {
                        moviesLocal.getMovies().remove(movieResultEntity);
                    }
                }
            }
        }
        return moviesLocal;
    }

    /**
     * showing search results provided by customSearchView request
     * @param moviesEntity
     */
    public void showSearchResult(MoviesEntity moviesEntity) {
        if (moviesEntity.getMovies().isEmpty()) {
            this.customToolbarSearchView.setVisibilityOfSearchResultBox(View.GONE);
        } else {
            this.customToolbarSearchView.setVisibilityOfSearchResultBox(View.VISIBLE);
        }

        this.searchResultMovieListAdapter.reloadWithNewResults(moviesEntity);
    }

    /**
     * getting movies by title and showing result in customSearchView result box
     * onNext: showing result in customSearchView result box
     */
    private class GetMovieByTitleObserver extends DisposableObserver<MoviesEntity> {

        @Override
        public void onComplete() {
            Log.d(TAG, "Subscribed to getMovieByTitle in SearchFeature");
        }

        @Override
        public void onNext(MoviesEntity moviesEntity) {
            showSearchResult(moviesEntity);
          // hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getLocalizedMessage());
            showSearchResult(new MoviesEntity());
         //   showToast(R.string.main_error);
          //  MainPresenter.this.hideLoading();
        }
    }

    public void destroy() {
        RxDisposeHelper.dispose(disposable);
        recyclerViewItemClickListener = null;
        searchResultMovieListAdapter = null;
        customToolbarSearchView = null;
        movieModel = null;
    }
}
