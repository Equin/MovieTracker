package com.example.movietracker.data.database;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

public class MyConverters {
    @TypeConverter
    public static Long dateToLong(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date longToDate(Long time) {
        return time == null ? null : new Date(time);
    }

    @TypeConverter
    public static List<Integer> fromString(String value) {
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<Integer> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
