package com.triskelapps.alcalasuena.model;


import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.api.common.ApiClient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by julio on 23/05/17.
 */

@Entity
public class Event implements Comparable {

    public static final int TIME_HOUR_MIDNIGHT_SAFE_THRESHOLD = 5; // After 5:00 is "next day"
    public static final int MIN_DISTANCE_TO_VENUE_HAPPENING_NOW = 100; // in meters
    public static final float MIN_ACCURACY_LOCATION_HAPPENING_NOW = 50; // in meters

    public static final String TIME = "time";
    public static final String DAY = "day";
    public static final String FAVOURITE = "favourite";
    public static final String ID = "id";
    public static final String STARRED = "starred";
    public static final String TIME_HOUR_MIDNIGHT_SAFE = "timeHourMidnightSafe";

    public static DateFormat dateFormatApi = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat dateFormatShareText = new SimpleDateFormat("EEE d MMMM");

    public static DateFormat timeFormatApi = new SimpleDateFormat("HH:mm:ss");
    public static DateFormat timeFormatUser = new SimpleDateFormat("HH:mm");


    // JSON fields
    @PrimaryKey
    private Integer id;
    private String day;
    private String time;
    private int duration;
    private String image;

    @SerializedName("tickets_url")
    private String ticketsUrl;

    private String bandsIdsStr;

    @Ignore
    @SerializedName("bands")
    private List<Integer> bandsIds;

    private int idVenue;


    // Processed fields
    private long timeHourMidnightSafe;

    @Ignore private boolean starred;
    @Ignore private Venue venue;
    @Ignore private transient List<Band> bands;



    public String getDayShareFormat() {
        try {
            Date dateDay = dateFormatApi.parse(day);
            return dateFormatShareText.format(dateDay);
        } catch (ParseException e) {
            e.printStackTrace();

            // Better than nothing :S
            return day.substring(8);
        }
    }

    public String getTimeFormatted() {
        try {
            Date timeDate = timeFormatApi.parse(time);
            return timeFormatUser.format(timeDate);
        } catch (ParseException e) {
            e.printStackTrace();

            // Better than nothing :S
            return time.substring(0, 5);
        }
    }

    public String getDurationFormatted(Context context) {
        return getDuration() + " " + context.getString(R.string.minutes);
    }


    public void configureTimeMidnightSafe() {

        try {
            Date timeDate = timeFormatApi.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeDate.getTime());
            int timeHour = calendar.get(Calendar.HOUR_OF_DAY);
            if (timeHour < TIME_HOUR_MIDNIGHT_SAFE_THRESHOLD) {
                calendar.add(Calendar.HOUR_OF_DAY, 24);
            }
            setTimeHourMidnightSafe((int) calendar.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public long getDayId() {

        try {
            Date date = dateFormatApi.parse(day);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            int dayInt = calendar.get(Calendar.DAY_OF_MONTH);
            return (long) dayInt;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalStateException("wrong date");
        }
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (int) (getTimeHourMidnightSafe() - ((Event) o).getTimeHourMidnightSafe());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public long getTimeHourMidnightSafe() {
        return timeHourMidnightSafe;
    }

    public void setTimeHourMidnightSafe(long timeHourMidnightSafe) {
        this.timeHourMidnightSafe = timeHourMidnightSafe;
    }

    public String getTicketsUrl() {
        return ticketsUrl;
    }

    public void setTicketsUrl(String ticketsUrl) {
        this.ticketsUrl = ticketsUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Uri getImageUrlFull() {
        if (getImage() == null) {
            return null;
        }

        String urlFull = ApiClient.BASE_URL + getImage();
        urlFull.replace("//", "/");
        return Uri.parse(urlFull);
    }

    public List<Band> getBands() {
        return bands;
    }

    public void setBands(List<Band> bands) {
        this.bands = bands;
    }

    public void addBand(Band band) {
        if (bands == null) {
            bands = new ArrayList<>();
        }
        bands.add(band);
    }

    public String getBandsNames() {
        String names = "";
        for (int i = 0; i < bands.size(); i++) {
            names += bands.get(i).getName() + (i < bands.size() - 1 ? " + " : "");
        }
        return names;
    }

    public List<Integer> getBandsIds() {
        return bandsIds;
    }

    public void setBandsIds(List<Integer> bandsIds) {
        this.bandsIds = bandsIds;
    }

    public String getBandsIdsStr() {
        return bandsIdsStr;
    }

    public void setBandsIdsStr(String bandsIdsStr) {
        this.bandsIdsStr = bandsIdsStr;
    }

    public String getDateTimeInfo(Context context) {
        SimpleDateFormat formatDateTimeApi = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatDateUser = new SimpleDateFormat("EEE, d 'de' MMMM");
        SimpleDateFormat formatTimeUser = new SimpleDateFormat("HH:mm");

        try {
            Date date = formatDateTimeApi.parse(getDay() + " " + getTime());
            String dateUser = formatDateUser.format(date);
            String timeUser = formatTimeUser.format(date);
            return context.getString(R.string.date_time_info_format, dateUser, timeUser, getDurationFormatted(context));

        } catch (ParseException e) {
            e.printStackTrace();
            return getDay() + "\n" + getTime() + "\n" + getDurationFormatted(context);
        }
    }

    public int getIdVenue() {
        return idVenue;
    }

    public void setIdVenue(int idVenue) {
        this.idVenue = idVenue;
    }

    public boolean mustShowBandInfo() {
        return getBands() != null && getBands().size() == 1;
    }
}
