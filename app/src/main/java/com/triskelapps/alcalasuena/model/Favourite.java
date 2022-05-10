package com.triskelapps.alcalasuena.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by julio on 25/05/17.
 */

@Entity
public class Favourite {

    public static final String STARRED = "starred";
    public static final String ID_EVENT = "idEvent";

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int idEvent;
    private boolean starred;


    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
