package com.triskelapps.alcalasuena.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Music style
 * Created by julio on 23/05/17.
 */

public class Tag extends RealmObject {


    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String color;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
