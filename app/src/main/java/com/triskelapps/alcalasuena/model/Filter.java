package com.triskelapps.alcalasuena.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julio on 26/05/17.
 */

public class Filter {

    private String day;
    private boolean starred;
    private List<Integer> tagsIds = new ArrayList<>();

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public List<Integer> getTagsIds() {
        return tagsIds;
    }

    public void setTagsIds(List<Integer> tagsIds) {
        this.tagsIds = tagsIds;
    }
}
