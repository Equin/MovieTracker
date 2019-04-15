package com.example.movietracker.view.model;

/**
 * Movie recycler item position for saving offset from top and position of clicked movie item.
 */
public class MovieRecyclerItemPosition {

    private int movieId;
    private int offset;

    private static class SingletonHelper {
        private static final MovieRecyclerItemPosition INSTANCE = new MovieRecyclerItemPosition();
    }

    public static MovieRecyclerItemPosition getInstance(){
        return SingletonHelper.INSTANCE;
    }

    private MovieRecyclerItemPosition() {
        this.movieId = 0;
        this.offset = 0;
    }

    public MovieRecyclerItemPosition(int movieId, int offset) {
        this.movieId = movieId;
        this.offset = offset;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setValuesToZero() {
        new MovieRecyclerItemPosition();
    }
}
