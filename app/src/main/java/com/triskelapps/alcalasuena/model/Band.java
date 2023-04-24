package com.triskelapps.alcalasuena.model;

import android.net.Uri;
import android.webkit.URLUtil;

import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.api.common.ApiConfig;

import static android.webkit.URLUtil.isValidUrl;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by julio on 18/05/17.
 */

@Entity
public class Band {

    public static final String URL_BAND_WEB = App.WEB_URL + "bands/";

    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String genre;
    private String profile_image;
    private String embed_code;
    private String band_image;
    private int num_members;
    private String city;
    private String facebook_link, twitter_link, youtube_link, bandcamp_link, presskit_link, webpage_link, instagram_link, spotify_link;

    private String idTag;

    @Ignore
    private Tag tag;

    public Uri getImageLogoUrlFull() {
        if (getProfile_image() == null) {
            return null;
        }

        String urlFull = ApiConfig.BASE_URL_MEDIA + getProfile_image();
        urlFull.replace("//", "/");
        return Uri.parse(urlFull);
    }


    public Uri getImageCoverUrlFull() {

        if (getBand_image() == null) {
            return getImageLogoUrlFull();
        }

        String urlFull = ApiConfig.BASE_URL_MEDIA + getBand_image();
        return Uri.parse(urlFull);
    }


    public boolean hasValidImage() {
        return getProfile_image() != null && URLUtil.isValidUrl(ApiConfig.BASE_URL_MEDIA + getProfile_image());
    }

    public String getGenreOrTag() {
        if (getGenre() != null && !getGenre().isEmpty()) {
            return getGenre();
        } else if (getTag() != null) {
            return getTag().getName();
        }
        return null;
    }

    public String getUrlBandWeb() {
        return URL_BAND_WEB + id;
    }

    // ---

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

    public void setName(String name) {
        this.name = name;
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

    public String getPresskit_link() {
        if (isValidUrl(presskit_link)) {
            return presskit_link;
        }
        return null;
    }

    public void setPresskit_link(String presskit_link) {
        this.presskit_link = presskit_link;
    }

    public String getSpotify_link() {
        return spotify_link;
    }

    public void setSpotify_link(String spotify_link) {
        this.spotify_link = spotify_link;
    }

    public String getInstagram_link() {
        return instagram_link;
    }

    public void setInstagram_link(String instagram_link) {
        this.instagram_link = instagram_link;
    }

    public String getWebpage_link() {
        return webpage_link;
    }

    public void setWebpage_link(String webpage_link) {
        this.webpage_link = webpage_link;
    }

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }
}
