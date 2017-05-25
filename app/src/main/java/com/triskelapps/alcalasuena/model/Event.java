package com.triskelapps.alcalasuena.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by julio on 23/05/17.
 */

public class Event extends RealmObject {


    @PrimaryKey private int id;
    private Date date;
    private String time;
    private Band band;
    private Venue venue;
    private transient Favourite starred;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Favourite isStarred() {
        return starred;
    }

    public void setStarred(Favourite starred) {
        this.starred = starred;
    }
}
