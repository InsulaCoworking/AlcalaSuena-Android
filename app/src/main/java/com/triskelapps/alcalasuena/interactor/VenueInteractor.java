package com.triskelapps.alcalasuena.interactor;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.triskelapps.alcalasuena.api.Api;
import com.triskelapps.alcalasuena.base.BaseInteractor;
import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.Venue;
import com.triskelapps.alcalasuena.util.Util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
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


    public void initializeVenuesFirstTime() {
        Log.i(TAG, "initializeVenuesFirstTime: start");
        String filePath = "venues.json";
        try {

            final String jsonTotal = Util.getStringFromAssets(context, filePath);
            final Gson gson = new GsonBuilder().create();

            Type listType = new TypeToken<List<Venue>>() {}.getType();
            final List<Venue> venues = gson.fromJson(jsonTotal, listType);

            storeVenues(venues);

            Log.i(TAG, "initializeVenuesFirstTime: end");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public List<Venue> getVenuesDB() {
        return Realm.getDefaultInstance().where(Venue.class).findAll();
    }

    public void getVenuesApi(final VenuesCallback callback) {

        if (!Util.isConnected(context)) {
//            baseView.toast(R.string.no_connection);
            return;
        }

//        baseView.setRefresing(true);

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

//                        baseView.setRefresing(false);

                        storeVenues(venues);
                        callback.onResponse(venues);


                    }
                });


    }

    private void storeVenues(List<Venue> venues) {

        for (Venue venue : venues) {
            for (Event event : venue.getEvents()) {
                for (int idBand : event.getBandsIds()) {
                    event.addBand(BandInteractor.getBandDB(idBand));
                }
                event.setBandsIdsStr(getStringCommaSeparated(event.getBandsIds()));
                event.configureTimeMidnightSafe();
                event.setVenue(venue);
            }
        }
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.where(Venue.class).findAll().deleteAllFromRealm();
        realm.where(Event.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();

        realm.beginTransaction();
        realm.insert(venues);
        realm.commitTransaction();
    }

    private String getStringCommaSeparated(List<Integer> ids) {
        String idsStr = "";
        for (int i = 0; i < ids.size(); i++) {
            idsStr += ids.get(i) + (i < ids.size() - 1 ? "," : "");
        }
        return idsStr;
    }

    public Venue getVenue(int idVenue) {
        return Realm.getDefaultInstance().where(Venue.class).equalTo(Venue.ID, idVenue).findFirst();
    }


    private Api getApi() {
        return getApi(Api.class);
    }


}
