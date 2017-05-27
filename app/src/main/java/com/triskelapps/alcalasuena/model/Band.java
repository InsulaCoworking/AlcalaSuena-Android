package com.triskelapps.alcalasuena.model;

import android.net.Uri;
import android.webkit.URLUtil;

import com.triskelapps.alcalasuena.api.common.ApiClient;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static android.webkit.URLUtil.isValidUrl;

/**
 * Created by julio on 18/05/17.
 */

public class Band extends RealmObject {

    public static final String NAME = "name";
    public static final String ID = "id";

    @PrimaryKey private int id;
    private String genre;
    private Tag tag;
    private String profile_image;
    private String description;
    private String name;
    private String embed_code;
    private String band_image;
    private int num_members;
    private String city;
    private String facebook_link, twitter_link, youtube_link, bandcamp_link, presskit_link;
    private RealmList<Event> events;

    public Uri getImageUrlFull() {
        if (getProfile_image() == null) {
            return null;
        }

        String urlFull = ApiClient.BASE_URL + getProfile_image();
        return Uri.parse(urlFull);
    }

    public boolean hasValidImage() {
        return getProfile_image() != null && URLUtil.isValidUrl(ApiClient.BASE_URL + getProfile_image());
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Band setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmbed_code() {
        return embed_code;
    }

    public void setEmbed_code(String embed_code) {
        this.embed_code = embed_code;
    }

    public String getBand_image() {
        return band_image;
    }

    public void setBand_image(String band_image) {
        this.band_image = band_image;
    }

    public int getNum_members() {
        return num_members;
    }

    public void setNum_members(int num_members) {
        this.num_members = num_members;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFacebook_link() {

        if (isValidUrl(facebook_link)) {
            return facebook_link;
        }
        return null;
    }

    public void setFacebook_link(String facebook_link) {
        this.facebook_link = facebook_link;
    }

    public String getTwitter_link() {

        if (isValidUrl(twitter_link)) {
            return twitter_link;
        }
        return null;
    }

    public void setTwitter_link(String twitter_link) {
        this.twitter_link = twitter_link;
    }

    public String getYoutube_link() {

        if (isValidUrl(youtube_link)) {
            return youtube_link;
        }
        return null;
    }

    public void setYoutube_link(String youtube_link) {
        this.youtube_link = youtube_link;
    }

    public String getBandcamp_link() {

        if (isValidUrl(bandcamp_link)) {
            return bandcamp_link;
        }
        return null;
    }

    public void setBandcamp_link(String bandcamp_link) {
        this.bandcamp_link = bandcamp_link;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", genre = " + genre + ", tag = " + tag + ", profile_image = " + profile_image + ", description = " + description + ", name = " + name + ", embed_code = " + embed_code + ", band_image = " + band_image + ", num_members = " + num_members + ", city = " + city + "]";
    }

    public RealmList<Event> getEvents() {
        return events;
    }

    public void setEvents(RealmList<Event> events) {
        this.events = events;
    }

    public String getPresskit_link() {
        if (isValidUrl(presskit_link)) {
            return presskit_link;
        }
        return null;
    }

    public void setPresskit_link(String presskit_link) {
        this.presskit_link = presskit_link;
    }

}
