package com.triskelapps.alcalasuena.model;

import android.net.Uri;
import android.webkit.URLUtil;

import com.triskelapps.alcalasuena.api.common.ApiClient;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by julio on 2/06/17.
 */

public class News extends RealmObject implements Serializable {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String TEXT = "text";
    public static final String IMAGE = "image";
    public static final String BTN_TEXT = "btn_text";
    public static final String BTN_LINK = "btn_link";
    public static final String NATIVE_SCREEN_CODE = "native_code";
    public static final String START_DATE_POPUP = "start_date";
    public static final String END_DATE_POPUP = "end_date";
    public static final String CADUCITY_DATE = "caducity";
    public static final String START_DATE_POPUP_TIME = "startDatePopupTime";
    public static final String END_DATE_POPUP_TIME = "endDatePopupTime";
    public static final String CADUCITY_DATE_TIME = "caducityDateTime";

    @PrimaryKey private int id;
    private String title;
    private String text;
    private String image;
    private String btn_text;
    private String btn_link;
    private Integer native_code;
    private String start_date;
    private String end_date;
    private String caducity;

    private Long startDatePopupTime;
    private Long endDatePopupTime;
    private Long caducityDateTime;


    public static DateFormat datetimeNewsFormatApiGet = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static DateFormat datetimeNewsFormatApiPost = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public boolean hasValidLinkButton() {
        return getBtn_text() != null && URLUtil.isValidUrl(getBtn_link());
    }

    public void configureDatesTime() {

        try {
            if (getStart_date() != null) {
                setStartDatePopupTime(datetimeNewsFormatApiGet.parse(getStart_date()).getTime());
            }

            if (getEnd_date() != null) {
                setEndDatePopupTime(datetimeNewsFormatApiGet.parse(getEnd_date()).getTime());
            }

            if (getCaducity() != null) {
                setCaducityDateTime(datetimeNewsFormatApiGet.parse(getCaducity()).getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public Uri getImageUrlFull() {
        if (getImage() == null) {
            return null;
        }

        String image = getImage();
//        if (image.startsWith("/")) {
//            image = image.substring(1);
//        }

        String urlFull = ApiClient.BASE_URL + image;
        urlFull.replace("//", "/");
        return Uri.parse(urlFull);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBtn_text() {
        return btn_text;
    }

    public void setBtn_text(String btn_text) {
        this.btn_text = btn_text;
    }

    public String getBtn_link() {
        return btn_link;
    }

    public void setBtn_link(String btn_link) {
        this.btn_link = btn_link;
    }

    public Integer getNative_code() {
        return native_code;
    }

    public void setNative_code(Integer native_code) {
        this.native_code = native_code;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getCaducity() {
        return caducity;
    }

    public void setCaducity(String caducity) {
        this.caducity = caducity;
    }

    public Long getStartDatePopupTime() {
        return startDatePopupTime;
    }

    public void setStartDatePopupTime(Long startDatePopupTime) {
        this.startDatePopupTime = startDatePopupTime;
    }

    public Long getEndDatePopupTime() {
        return endDatePopupTime;
    }

    public void setEndDatePopupTime(Long endDatePopupTime) {
        this.endDatePopupTime = endDatePopupTime;
    }

    public Long getCaducityDateTime() {
        return caducityDateTime;
    }

    public void setCaducityDateTime(Long caducityDateTime) {
        this.caducityDateTime = caducityDateTime;
    }

}
