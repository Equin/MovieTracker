package com.example.movietracker.view.helper;

import com.example.movietracker.data.entity.genre.GenreEntity;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * The type Utility helpers.
 */
public class UtilityHelpers {

    private UtilityHelpers() {

    }

    /**
     * Gets year  in format YYYY from date .
     *
     * @param date the date
     * @return the year
     */
    public static String getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getAppropriateValue(calendar.get(Calendar.YEAR));
    }

    /**
     * Gets string from value or "" if null
     *
     * @param value the value
     * @return the appropriate string value
     */
    public static String getAppropriateValue(Object value) {
        return value == null ? "" : value.toString();
    }

    /**
     * Gets pipe divided genres.
     *
     * @param genres the genres
     * @return the string pipe divided genres
     */
    public static String getPipeDividedGenres(List<GenreEntity> genres) {
        StringBuilder stringBuilder = new StringBuilder();

        if (genres == null) return "";

        for(int i = 0; i<genres.size(); i++) {
            stringBuilder.append(genres.get(i).getGenreName());
            if(i != genres.size()-1) {
                stringBuilder.append(" | ");
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Gets pipe divided genres from genres and int genreIds
     *
     * @param genresId the genres id
     * @param genres   the genres
     * @return the pipe divided genres from id
     */
    public static String getPipeDividedGenresFromId(List<Integer> genresId, List<GenreEntity> genres) {
        List<GenreEntity> tempGenres = new ArrayList<>();

        for(int i = 0; i<genresId.size(); i++) {
            if(genres != null) {
                for (int j = 0; j < genres.size(); j++) {
                    if(genres.get(j).getGenreId() == genresId.get(i)) {
                        tempGenres.add(genres.get(j));
                    }
                }
            }
        }

        return getPipeDividedGenres(tempGenres);
    }
}
