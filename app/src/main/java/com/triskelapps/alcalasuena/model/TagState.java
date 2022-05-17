package com.triskelapps.alcalasuena.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TagState {

    @PrimaryKey
    @NonNull
    private String idTag;
    private boolean active = true;

    public TagState() {
    }

    public TagState(String idTag) {
        this.idTag = idTag;
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
