package com.triskelapps.alcalasuena.model;

public class SocialItem {

    private int iconId;
    private String url;


    public SocialItem(int iconId, String url) {
        this.iconId = iconId;
        this.url = url;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getUrl() {
        if (!url.startsWith("http")) {
            url = "http://" + url.replace("://", "");
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
