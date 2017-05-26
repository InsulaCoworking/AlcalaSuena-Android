package com.triskelapps.alcalasuena.interactor;

import android.content.Context;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.api.Api;
import com.triskelapps.alcalasuena.base.BaseInteractor;
import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.Venue;
import com.triskelapps.alcalasuena.util.Util;

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


    public List<Venue> getVenuesDB() {
        return Realm.getDefaultInstance().where(Venue.class).findAll();
    }

    public void getVenuesApi(final VenuesCallback callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        baseView.setRefresing(true);

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

                        baseView.setRefresing(false);

                        storeVenues(venues);
                        callback.onResponse(venues);


                    }
                });


    }

    private void storeVenues(List<Venue> venues) {

        for (Venue venue : venues) {
            for (Event event : venue.getEvents()) {
                event.setBandEntity(BandInteractor.getBandDB(event.getBand()));
                event.setVenue(venue);
            }
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insert(venues);
        realm.commitTransaction();
    }


    private Api getApi() {
        return getApi(Api.class);
    }


}
