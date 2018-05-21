package com.triskelapps.alcalasuena.model.notification;

public class FirebasePush {


    public static final String NOTIFICATION_TITLE = "title";
    public static final String NOTIFICATION_MESSAGE = "message";
    public static final String NOTIFICATION_ID_NEWS = "id_news";
    public static final String NOTIFICATION_CUSTOM_BUTTON_TEXT = "btn_text";
    public static final String NOTIFICATION_CUSTOM_BUTTON_LINK = "btn_link";
    public static final String NOTIFICATION_NEWS = "newsJson";

    public static final String KEY_NEWS_ID = "id_news";

    public static final String EXTRA_OPEN_URL_LINK = "extra_open_url_link";

    private String to;
    private FirebasePushNotification notification;
    private FirebasePushData data;


    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public FirebasePushNotification getNotification() {
        return notification;
    }

    public void setNotification(FirebasePushNotification notification) {
        this.notification = notification;
    }

    public FirebasePushData getData() {
        return data;
    }

    public void setData(FirebasePushData data) {
        this.data = data;
    }
}
