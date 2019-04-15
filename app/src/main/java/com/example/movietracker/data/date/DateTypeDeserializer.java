package com.example.movietracker.data.date;
import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The Date type deserializer for parsing date replacing null values with "1000-08-28"
 */
public class DateTypeDeserializer implements JsonDeserializer<Date> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TAG = DateTypeDeserializer.class.getCanonicalName();

    @Override
    public Date deserialize(JsonElement jsonElement, Type typeOF, JsonDeserializationContext context) throws JsonParseException {
        String date = jsonElement.getAsString().length() != 0 ? jsonElement.getAsString() : "1000-08-28";

            try {
                return new SimpleDateFormat(DATE_FORMAT, Locale.US).parse(date);
            } catch (ParseException e) {
                Log.e(TAG, e.getLocalizedMessage());
            }

        throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                + "\". Supported formats: \n" + DATE_FORMAT);
    }
}