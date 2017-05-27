package com.triskelapps.alcalasuena.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Music style
 * Created by julio on 23/05/17.
 */

public class Tag extends RealmObject {


    public static final String ID = "id";
    public static final String ACTIVE = "active";

    @PrimaryKey
    private String id;
    private String name;
    private String description;
    private String color;
    private boolean active = true;


    public Tag(String id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Tag() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
