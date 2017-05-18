package com.triskelapps.alcalasuena.api;


import com.triskelapps.alcalasuena.model.Band;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface BandApi {

    @GET("bands")
    Observable<List<Band>> getBands();

}
