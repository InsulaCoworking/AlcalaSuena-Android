package com.triskelapps.alcalasuena.interactor;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.api.Api;
import com.triskelapps.alcalasuena.base.BaseInteractor;
import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.Venue;
import com.triskelapps.alcalasuena.util.Util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class VenueInteractor extends BaseInteractor {


    public interface VenuesCallback {

        void onResponse(List<Venue> venues);

        void onError(String error);
    }

    public VenueInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }


    public void getVenuesApi(final VenuesCallback callback) {

        if (!Util.isConnected(context)) {
            return;
        }

        getApi().getVenues()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(actionTerminate)
                .subscribe(new Observer<List<Venue>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Venue> venues) {

                        storeVenues(venues);
                        callback.onResponse(venues);


                    }
                });


    }

    private void storeVenues(List<Venue> venues) {

        App.getDB().venueDao().deleteAll();
        App.getDB().eventDao().deleteAll();

        App.getDB().venueDao().insertAll(venues);

        venues.stream().forEach(venue -> {
            venue.getEvents().stream().forEach(event -> {
                event.setBandsIdsStr(getStringCommaSeparated(event.getBandsIds()));
                event.configureTimeMidnightSafe();
                event.setIdVenue(venue.getId());
            });
            App.getDB().eventDao().insertAll(venue.getEvents());
        });
    }

    private String getStringCommaSeparated(List<Integer> ids) {

        if (ids.isEmpty()) {
            Log.i(TAG, "getStringCommaSeparated: ");
        }

        String commaSeparatedNumbers = ids.stream()
                .map(i -> String.valueOf(i))
                .collect(Collectors.joining(", "));

        return commaSeparatedNumbers;

//        String idsStr = "";
//        for (int i = 0; i < ids.size(); i++) {
//            idsStr += ids.get(i) + (i < ids.size() - 1 ? "," : "");
//        }
//        return idsStr;
    }

    private Api getApi() {
        return getApi(Api.class);
    }


}
