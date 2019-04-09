package com.example.movietracker.data.entity;

import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.view.model.Order;
import com.example.movietracker.view.model.SortBy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Filters implements Serializable {

    private int page;
    private List<Integer> genresId;
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
        genresId = new ArrayList<>();
        selectedGenres = new ArrayList<>();
        order = Order.DESC;
    }

    public Filters(int page, List<Integer> genresId, boolean includeAdult, String sortBy, Order order) {
        this.page = page;
        this.genresId = genresId;
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

    public void setGenresId(@NonNull List<Integer> genresId) {
        this.genresId = genresId;
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

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Nullable
    public Order getOrder() {
        return order;
    }

    public void setOrder(@NonNull Order order) {
        this.order = order;
    }

    public void setSelectedGenres(@NonNull GenresEntity genresEntity) {
        this.selectedGenres = new ArrayList<>();
        if (genresEntity.getGenres() != null) {
            for (GenreEntity genre : genresEntity.getGenres()) {
                if (genre.isSelected()) {
                    this.selectedGenres.add(genre);
                }
            }
        }
    }

    public List<GenreEntity> getSelectedGenres() {
        return this.selectedGenres;
    }

    public void incrementPage() {
        this.page++;
    }

    public List<Integer> getGenresId() {
        for(GenreEntity genre : getSelectedGenres()) {
            genresId.add(genre.getGenreId());
        }
        return genresId;
    }

    public String getCommaSeparatedGenres() {
        List<GenreEntity> genreEntity = getSelectedGenres();
        StringBuilder sb = new StringBuilder();

        for (GenreEntity genre : genreEntity) {
            sb.append(genre.getGenreId()).append(",");
        }
        return sb.toString();
    }
}

