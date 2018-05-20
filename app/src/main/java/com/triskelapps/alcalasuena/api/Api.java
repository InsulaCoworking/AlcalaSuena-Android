package com.triskelapps.alcalasuena.api;


import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.notification.FirebasePush;
import com.triskelapps.alcalasuena.model.News;
import com.triskelapps.alcalasuena.model.Venue;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    @GET("settings/last_data_version")
    Observable<String> getLastDataVersion();


    @GET("news")
    Observable<List<News>> getNews();


    @Multipart
    @POST("https://alcalasuena.es/news/add/")
    Observable<Response<Void>> sendNews(@Part MultipartBody.Part filePart, @PartMap Map<String, RequestBody> params);

    @POST("https://fcm.googleapis.com/fcm/send")
    Observable<Response<Void>> sendPush(@Header("Authorization") String serverKey, @Body FirebasePush firebasePush);

}
