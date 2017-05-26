package com.triskelapps.alcalasuena.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by julio on 23/05/17.
 */

public class Event extends RealmObject {


    public static final String TIME = "time";
    public static final String DAY = "day";
    public static final String FAVOURITE = "favourite";
    public static final String ID = "id";
    public static final String STARRED = "starred";


    @PrimaryKey private int id;
    private String day;
    private String time;
    private int band;
    private int duration;
    private boolean starred;
    private Band bandEntity;
    private Venue venue;

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

    public int getBand() {
        return band;
    }

    public void setBand(int band) {
        this.band = band;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Band getBandEntity() {
        return bandEntity;
    }

    public void setBandEntity(Band bandEntity) {
        this.bandEntity = bandEntity;
    }

    public String getTimeFormatted() {
        String time = getTime();
        return time.substring(0, 5);
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
}
