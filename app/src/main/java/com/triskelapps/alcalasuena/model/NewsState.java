package com.triskelapps.alcalasuena.model;

import io.realm.RealmObject;

/**
 * Created by julio on 25/05/17.
 */

public class NewsState extends RealmObject {

    public static final String ID_NEW = "idNews";
    public static final String READ = "read";
    public static final String SEEN = "seen";

    private int idNews;
    private boolean read;
    private boolean seen;

    public int getIdNews() {
        return idNews;
    }

    public void setIdNews(int idNews) {
        this.idNews = idNews;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isSeen() {
        return seen;
    }

    public NewsState setSeen(boolean seen) {
        this.seen = seen;
        return this;
    }
}
