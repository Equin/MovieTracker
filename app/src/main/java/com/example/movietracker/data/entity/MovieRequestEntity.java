package com.example.movietracker.data.entity;

import com.example.movietracker.data.entity.genre.GenreEntity;
import com.example.movietracker.data.entity.genre.GenresEntity;
import com.example.movietracker.view.model.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MovieRequestEntity implements Serializable {

    private int page;
    private List<Integer> genresId;
    private GenresEntity genresEntity;
    private boolean includeAdult;
    private String sortBy;
    private Order order;

    public MovieRequestEntity() {
        page = 1;
        sortBy = "Popularity";
        genresId = new ArrayList<>();
        genresEntity = new GenresEntity();
        order = Order.DESC;
    }

    public MovieRequestEntity(int page, List<Integer> genresId, GenresEntity genresEntity, boolean includeAdult, String sortBy, Order order) {
        this.page = page;
        this.genresId = genresId;
        this.genresEntity = genresEntity;
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

    public void setGenresId(List<Integer> genresId) {
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public GenresEntity getGenresEntity() {
        return genresEntity;
    }

    public void setGenresEntity(GenresEntity genresEntity) {
        this.genresEntity = genresEntity;
    }

    public GenresEntity getSelectedGenres() {
        GenresEntity genres = new GenresEntity();
        List<GenreEntity> genreList = new ArrayList<>();

        if (genresEntity != null) {
            for (GenreEntity genre : genresEntity.getGenres()) {
                if (genre.isSelected()) {
                    genreList.add(genre);
                }
            }
        }

        genres.setGenres(genreList);

        return genres;
    }

    public void incrementPage() {
        this.page++;
    }

    public List<Integer> getGenresId() {
        GenresEntity genresEntity = getSelectedGenres();

        for(GenreEntity genre : genresEntity.getGenres()) {
            genresId.add(genre.getGenreId());
        }
        return genresId;
    }

    public String getCommaSeparatedGenres() {
        List<GenreEntity> genreEntity = getSelectedGenres().getGenres();
        StringBuilder sb = new StringBuilder();

        for (GenreEntity genre : genreEntity) {
            sb.append(genre.getGenreId()).append(",");
        }
        return sb.toString();
    }
}

