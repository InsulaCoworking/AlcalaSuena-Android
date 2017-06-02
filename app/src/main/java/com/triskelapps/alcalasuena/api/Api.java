package com.triskelapps.alcalasuena.api;


import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.News;
import com.triskelapps.alcalasuena.model.Venue;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface Api {

    @GET("bands")
    Observable<List<Band>> getBands();

    @GET("event")
    Observable<List<Event>> getEvents();

    @GET("venues")
    Observable<List<Venue>> getVenues();

    @GET("settings/app_version_market")
    Observable<String> getAppVersionInMarket();

    @GET("news")
    Observable<List<News>> getNews();

}
