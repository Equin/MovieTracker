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
    private String searchQueryByTitle;

    private static class SingletonHelper {
        private static final Filters INSTANCE = new Filters();
    }

    public static Filters getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private Filters() {
        this.page = 1;
        this.sortBy = SortBy.POPULARITY.getSearchName();
        this.selectedGenresIds = new ArrayList<>();
        this.selectedGenres = new ArrayList<>();
        this.order = Order.DESC;
        this.searchQueryByTitle = "";
        includeAdult = true;
    }

    public Filters(int page, @NonNull List<GenreEntity> selectedGenres, boolean includeAdult, @NonNull String sortBy, @NonNull Order order, @NonNull String searchQueryByTitle ) {
        this.page = page;
        this.selectedGenres = selectedGenres;
        this.includeAdult = includeAdult;
        this.sortBy = sortBy;
        this.order = order;
        this.searchQueryByTitle = searchQueryByTitle;
    }

    public int getPage() {
        return  this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isIncludeAdult() {
        return  this.includeAdult;
    }

    public void setIncludeAdult(boolean includeAdult) {
        this.includeAdult = includeAdult;
    }

    public String getSortBy() {
        return  this.sortBy;
    }

    public void setSortBy(@NonNull String sortBy) {
        this.sortBy = sortBy;
    }

    @Nullable
    public Order getOrder() {
        return  this.order;
    }

    public void setOrder(@NonNull Order order) {
        this.order = order;
    }

    public String getSearchQueryByTitle() {
        return  this.searchQueryByTitle;
    }

    public void setSearchQueryByTitle(String searchQueryByTitle) {
        this.searchQueryByTitle = searchQueryByTitle;
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
        return  this.selectedGenresIds;
    }

    public String getCommaSeparatedGenres() {
        List<GenreEntity> genreEntity = getSelectedGenres();
        StringBuilder sb = new StringBuilder();

        for (GenreEntity genre : genreEntity) {
            sb.append(genre.getGenreId()).append(",");
        }
        return sb.toString();
    }

    private Filters(FiltersBuilder filtersBuilder) {
        getInstance().setPage(filtersBuilder.page);
        getInstance().setIncludeAdult(filtersBuilder.includeAdult);
        getInstance().setSortBy(filtersBuilder.sortBy);
        getInstance().setOrder(filtersBuilder.order);
        getInstance().setSearchQueryByTitle(filtersBuilder.searchQueryByTitle);

        this.selectedGenres = filtersBuilder.selectedGenres;
        this.selectedGenresIds = filtersBuilder.selectedGenresIds;

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

    public static class FiltersBuilder {

        public FiltersBuilder(Filters filters) {
            this.page = filters.page;
            this.selectedGenresIds = filters.selectedGenresIds;
            this.selectedGenres = filters.selectedGenres;
            this.includeAdult = filters.includeAdult;
            this.sortBy = filters.sortBy;
            this.order = filters.order;
            this.searchQueryByTitle = filters.searchQueryByTitle;
        }

        public FiltersBuilder() {}

        private int page;
        private List<Integer> selectedGenresIds;
        private List<GenreEntity> selectedGenres;
        private boolean includeAdult;
        private String sortBy;
        private Order order;
        private String searchQueryByTitle;


        public FiltersBuilder setPage(int page) {
            this.page = page;
            return this;
        }

        public FiltersBuilder setSelectedGenresIds(List<Integer> selectedGenresIds) {
            this.selectedGenresIds = selectedGenresIds;
            return this;
        }

        public FiltersBuilder setSelectedGenres(List<GenreEntity> selectedGenres) {
            this.selectedGenres = selectedGenres;
            return this;
        }

        public FiltersBuilder setIncludeAdult(boolean includeAdult) {
            this.includeAdult = includeAdult;
            return this;
        }

        public FiltersBuilder setSortBy(String sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        public FiltersBuilder setOrder(Order order) {
            this.order = order;
            return this;
        }

        public FiltersBuilder setSearchQueryByTitle(String searchQueryByTitle) {
            this.searchQueryByTitle = searchQueryByTitle;
            return this;
        }
        public Filters build() {
            return new Filters(this);
        }
    }
}

