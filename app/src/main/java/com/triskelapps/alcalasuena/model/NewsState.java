package com.triskelapps.alcalasuena.model;

import io.realm.RealmObject;

/**
 * Created by julio on 25/05/17.
 */

public class NewsState extends RealmObject {

    public static final String ID_NEWS = "idNews";
    public static final String READ = "read";
    public static final String SEEN = "seen";

    private int idNews;
    private Boolean read;
    private Boolean seen;

    public Integer getIdNews() {
        return idNews;
    }

    public void setIdNews(Integer idNews) {
        this.idNews = idNews;
    }

    public Boolean isRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean isSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}
