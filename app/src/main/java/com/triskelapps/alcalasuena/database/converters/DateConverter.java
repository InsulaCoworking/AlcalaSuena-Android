package com.triskelapps.alcalasuena.database.converters;

import androidx.room.TypeConverter;

import com.triskelapps.alcalasuena.util.DateUtils;

import java.text.ParseException;
import java.util.Date;

public class DateConverter {

    private DateConverter() {
        throw new IllegalStateException("Utility class");
    }

    @TypeConverter
    public static Date fromTimestamp(String value) {
        try {
            return value == null ? null : DateUtils.formatDateTimeApi.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public static String toTimestamp(Date dateTime) {
        return DateUtils.formatDateTimeApi.format(dateTime);
    }

}
