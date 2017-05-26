package com.triskelapps.alcalasuena.model;

import io.realm.RealmObject;

/**
 * Created by julio on 25/05/17.
 */

public class Favourite extends RealmObject {

    public static final String STARRED = "starred";
    public static final String ID_EVENT = "idEvent";

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
}
