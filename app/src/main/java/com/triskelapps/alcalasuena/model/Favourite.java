package com.triskelapps.alcalasuena.model;

import io.realm.RealmObject;

/**
 * Created by julio on 25/05/17.
 */

public class Favourite extends RealmObject {

    private boolean starred;


    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }
}
