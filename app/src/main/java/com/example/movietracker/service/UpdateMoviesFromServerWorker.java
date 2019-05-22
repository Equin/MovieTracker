package com.example.movietracker.service;

import android.content.Context;
import android.util.Log;

import com.example.movietracker.data.entity.movie.MovieChangesResultEntity;
import com.example.movietracker.data.entity.movie.MovieResultEntity;
import com.example.movietracker.di.ClassProvider;
import com.example.movietracker.model.ModelContract;
import com.example.movietracker.model.model_impl.MovieDetailTabsModelImpl;
import com.example.movietracker.model.model_impl.MovieInfoModelImpl;
import com.example.movietracker.model.model_impl.MovieModelImpl;
import com.example.movietracker.notification.PushNotificationSender;
import com.example.movietracker.view.model.Filters;
import com.example.movietracker.view.model.PushNotificationsMovieInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.reactivex.Observable;

/**
 * The type Update movies from server worker.
 */
public class UpdateMoviesFromServerWorker extends Worker {

    private static final String TAG = UpdateMoviesFromServerWorker.class.getCanonicalName();
    private static final int MOVIE_COUNT_FOR_REQUEST = 2;
    private static final int DELAY_BETWEEN_REQUEST_MILLISECONDS = 10000;
    private  Context context;
    private PushNotificationSender pushNotificationSender;

    public UpdateMoviesFromServerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        ClassProvider.initialize(context);
        pushNotificationSender = new PushNotificationSender(context);

        updateMoviesFromServer();

        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        ClassProvider.onDestroy();
    }

    /**
     * getting all movies ids from DB, placing 2 id into list, making request for both ids and clean list after request for new emit of ids.
     */
    private void updateMoviesFromServer() {
        ModelContract.MovieModel movieModel = new MovieModelImpl();

        List<Integer> moviesIds = new ArrayList<>();
        movieModel.getMoviesIdList().flatMapIterable(x -> x).collect(() -> moviesIds, (listWithTwoIds, p) -> {
            listWithTwoIds.add(p);
            if(listWithTwoIds.size() == MOVIE_COUNT_FOR_REQUEST) {
                Thread.sleep(DELAY_BETWEEN_REQUEST_MILLISECONDS); // pausing each next request for 10 seconds... need to replace with RxJava but haven`t found how to do it
                Observable.fromIterable(listWithTwoIds).doOnNext(this::makeRequests).subscribe();
                Log.e(TAG, "---------------------- ");
                listWithTwoIds.clear();
            } }).subscribe();


        movieModel.getMoviesChanges().flatMap(movieChangesEntity -> {
            return movieModel.getMoviesWithFavorites(Filters.getInstance()).doOnNext(moviesEntity -> {
                PushNotificationsMovieInfo pushNotificationsMovieInfo = new PushNotificationsMovieInfo();
                for(MovieResultEntity movieResultEntity : moviesEntity.getMovies()) {
                    MovieChangesResultEntity movieEntity =
                            new MovieChangesResultEntity(movieResultEntity.getMovieId(), movieResultEntity.isAdult());

                    if(movieChangesEntity.getResults().contains(movieEntity)) {
                        pushNotificationsMovieInfo.addMoiveTitleItemToList(movieResultEntity.getMovieTitle());
                    }
                }
                pushNotificationSender.sendNotification(pushNotificationsMovieInfo, 0);
            });
        }).subscribe();
    }

    /**
     * getting details for each movie, each movie details request getting info from Server and saving it to Room DB
     *
     * @param movieId
     */
    private void makeRequests(int movieId) {
        ModelContract.MovieInfoModel movieInfoModel= new MovieInfoModelImpl();
        ModelContract.MovieDetailTabsModel movieDetailTabsModel = new MovieDetailTabsModelImpl();

        Log.e(TAG, "updating movie with id " + movieId);

        Observable.zip(
                movieInfoModel.getMovieInfo(movieId),
                movieDetailTabsModel.getMovieCasts(movieId),
                movieDetailTabsModel.getMovieReviews(movieId),
                movieDetailTabsModel.getMovieVideos(movieId),
                (movieInfo, movieCasts, movieReviews, movieVideos) -> movieInfo).subscribe();
    }
}
