package com.example.movietracker.view.model;

import com.example.movietracker.data.entity.genre.GenreEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Filters implements Serializable {

    private int page;
    private List<Integer> selectedGenresIds;
    private List<GenreEntity> selectedGenres;
    private boolean includeAdult;
    private String sortBy;
    private Order order;

    private static class SingletonHelper {
        private static final Filters INSTANCE = new Filters();
    }

    public static Filters getInstance(){
        return SingletonHelper.INSTANCE;
    }

    private Filters() {
        page = 1;
        sortBy = SortBy.POPULARITY.getSearchName();
        selectedGenresIds = new ArrayList<>();
        selectedGenres = new ArrayList<>();
        order = Order.DESC;
    }

    public void clearGenreFilters() {
        selectedGenresIds = new ArrayList<>();
        selectedGenres = new ArrayList<>();
    }

    public Filters(int page, @NonNull List<GenreEntity> selectedGenres, boolean includeAdult, @NonNull String sortBy, @NonNull Order order) {
        this.page = page;
        this.selectedGenres = selectedGenres;
        this.includeAdult = includeAdult;
        this.sortBy = sortBy;
        this.order = order;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isIncludeAdult() {
        return includeAdult;
    }

    public void setIncludeAdult(boolean includeAdult) {
        this.includeAdult = includeAdult;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(@NonNull String sortBy) {
        this.sortBy = sortBy;
    }

    @Nullable
    public Order getOrder() {
        return order;
    }

    public void setOrder(@NonNull Order order) {
        this.order = order;
    }

    public void addSelectedGenre(@NonNull GenreEntity genreEntity) {
        this.selectedGenres.add(genreEntity);
        this.selectedGenresIds.add(genreEntity.getGenreId());
    }

    public void removeUnselectedGenre(@NonNull GenreEntity genreEntity) {
      this.selectedGenres.remove(genreEntity);
      this.selectedGenresIds.remove(
                this.selectedGenresIds.indexOf(genreEntity.getGenreId()));
    }

    public List<GenreEntity> getSelectedGenres() {
        return this.selectedGenres;
    }

    public void incrementPage() {
        this.page++;
    }

    public List<Integer> getSelectedGenresIds() {
        return selectedGenresIds;
    }

    public String getCommaSeparatedGenres() {
        List<GenreEntity> genreEntity = getSelectedGenres();
        StringBuilder sb = new StringBuilder();

        for (GenreEntity genre : genreEntity) {
            sb.append(genre.getGenreId()).append(",");
        }
        return sb.toString();
    }

    /*    public void setSelectedGenres(@NonNull GenresEntity genresEntity) {

        if (genresEntity.getGenres() != null) {
            for (GenreEntity genre : genresEntity.getGenres()) {
                if (genre.isSelected()) {
                    this.selectedGenres.add(genre);
                } else {
                    this.selectedGenres.remove(genre);
                }
            }

            this.setSelectedGenresIds();
        }
    }*/

/*
    private void setSelectedGenresIds() {
        selectedGenresIds = new ArrayList<>();
        for (GenreEntity genreEntity : this.getSelectedGenres()) {
            if(genreEntity.isSelected()) {
                this.selectedGenresIds.add(genreEntity.getGenreId());
            }
        }
    }
*/
}
