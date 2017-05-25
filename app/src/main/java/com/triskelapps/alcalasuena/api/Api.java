package com.triskelapps.alcalasuena.api;


import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface Api {

    @GET("bands")
    Observable<List<Band>> getBands();

    @GET("event")
    Observable<List<Event>> getEvents();

}
