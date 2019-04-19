package com.example.movietracker.service;

import android.content.Context;
import android.util.Log;

import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.model.model_impl.MovieDetailTabsModelImpl;
import com.example.movietracker.model.model_impl.MovieInfoModelImpl;
import com.example.movietracker.model.model_impl.MovieModelImpl;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.reactivex.Observable;

public class UpdateMoviesFromServerWorker extends Worker {

    private  Context context;

    public UpdateMoviesFromServerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        updateMoviesFromServer();
        ClassProvider.initialize(context);
      //  ClassProvider.onDestroy();

        return Result.success();
    }

    private void updateMoviesFromServer() {
        ModelContract.MovieModel movieModel = new MovieModelImpl();

        List<Integer> phraseIDs = new ArrayList<>();
        movieModel.getMoviesIdList().flatMapIterable(x -> x).collect(() -> phraseIDs, (listWithTwoIds, p) -> {
            listWithTwoIds.add(p);
            if(listWithTwoIds.size() == 2) {
                Thread.sleep(10000);
                Observable.fromIterable(listWithTwoIds).doOnNext(integer -> makeRequests(integer).subscribe()).subscribe();
                Log.e("UPDATE_SERVICE", "---------------------- ");
                listWithTwoIds.clear();
            } }).subscribe();
    }

    private Observable<Object> makeRequests(int movieId) {
        ClassProvider.initialize(context);
        ModelContract.MovieInfoModel movieInfoModel= new MovieInfoModelImpl();
        ModelContract.MovieDetailTabsModel movieDetailTabsModel = new MovieDetailTabsModelImpl();
        Log.e("UPDATE_SERVICE", "runs " + movieId);

       return Observable.zip(
                movieInfoModel.getMovieInfo(movieId),
                movieDetailTabsModel.getMovieCasts(movieId),
                movieDetailTabsModel.getMovieReviews(movieId),
                movieDetailTabsModel.getMovieVideos(movieId), (o, b,c, d)-> o);
    }
}
