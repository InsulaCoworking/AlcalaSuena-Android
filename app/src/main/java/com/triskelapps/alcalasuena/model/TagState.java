package com.triskelapps.alcalasuena.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TagState {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String idTag;
    private boolean active = true;

    public TagState() {
    }

    public TagState(String idTag) {
        this.idTag = idTag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }
}
