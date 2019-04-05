package com.example.movietracker.data.date;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.provider.Settings.System.DATE_FORMAT;

public class DateSerializer implements JsonSerializer<Date> {

    private final String TAG = DateSerializer.class.getCanonicalName();

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        formatter.setTimeZone(TimeZone.getDefault());
        String dateFormatAsString = formatter.format(date);
        return new JsonPrimitive(dateFormatAsString);
    }
}