package com.example.movietracker.view.helper;

import com.example.movietracker.data.entity.GenreEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UtilityHelpers {

    private UtilityHelpers() {

    }

    public static String getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getAppropriateValue(calendar.get(Calendar.YEAR));
    }

    public static String getAppropriateValue(Object value) {
        return value == null ? "" : value.toString();
    }

    public static String getPipeDividedGenres(List<GenreEntity> genres) {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i<genres.size(); i++) {
            stringBuilder.append(genres.get(i).getGenreName());
            if(i != genres.size()-1) {
                stringBuilder.append(" | ");
            }
        }

        return stringBuilder.toString();
    }
}
