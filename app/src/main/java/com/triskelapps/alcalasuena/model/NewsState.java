package com.triskelapps.alcalasuena.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by julio on 25/05/17.
 */

@Entity
public class NewsState {

    @PrimaryKey(autoGenerate = true)
    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
