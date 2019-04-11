package com.triskelapps.alcalasuena.model;

import com.triskelapps.alcalasuena.api.common.ApiClient;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by julio on 23/05/17.
 */

public class Venue extends RealmObject implements Serializable {

    public static final String ID = "id";


    @PrimaryKey private int id;
    private String name;
    private String description;
    private String image;
    private String address;
    private double latitude;
    private double longitude;
    private RealmList<Event> events;


    public String getImageUrlFull() {
        if (getImage() == null) {
            return null;
        }

        String image = getImage();
//        if (image.startsWith("/")) {
//            image = image.substring(1);
//        }

        String urlFull = ApiClient.BASE_URL + image;
        return urlFull;
    }


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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public RealmList<Event> getEvents() {
        return events;
    }

    public void setEvents(RealmList<Event> events) {
        this.events = events;
    }
}
