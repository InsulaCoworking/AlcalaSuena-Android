package com.triskelapps.alcalasuena.model.notification;


public class FirebasePushData {
    private String id_news;
    private String title;
    private String message;
    private String btn_link;
    private String btn_text;

    public String getId_news() {
        return id_news;
    }

    public void setId_news(String id_news) {
        this.id_news = id_news;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBtn_link() {
        return btn_link;
    }

    public void setBtn_link(String btn_link) {
        this.btn_link = btn_link;
    }

    public String getBtn_text() {
        return btn_text;
    }

    public void setBtn_text(String btn_text) {
        this.btn_text = btn_text;
    }
}
