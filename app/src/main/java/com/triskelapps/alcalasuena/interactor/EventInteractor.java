package com.triskelapps.alcalasuena.interactor;

import android.content.Context;

import com.triskelapps.alcalasuena.api.Api;
import com.triskelapps.alcalasuena.base.BaseInteractor;
import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.Favourite;
import com.triskelapps.alcalasuena.model.Filter;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.Sort;

/**
 * Created by julio on 14/02/16.
 */
public class EventInteractor extends BaseInteractor {


    public EventInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }


    public List<Event> getEventsDB(Filter filter) {
        resetStarredState();

        RealmQuery<Event> query = Realm.getDefaultInstance().where(Event.class);

        if (filter.getDay() != null) {
            query.equalTo(Event.DAY, filter.getDay());
        }

        if (filter.isStarred()) {
            query.equalTo(Event.STARRED, true);
        } else {
            query.equalTo("bandEntity.tag.active", true);
        }


        List<Event> events = query.findAllSorted(Event.TIME_HOUR_MIDNIGHT_SAFE);

        return events;
    }


    public List<Event> getEventsForBand(int idBand) {

        resetStarredState();
        return Realm.getDefaultInstance().where(Event.class)
                .equalTo(Event.BAND_ID, idBand)
                .findAllSorted(Event.DAY, Sort.ASCENDING, Event.TIME_HOUR_MIDNIGHT_SAFE, Sort.ASCENDING);
    }


    public List<Event> getEventsForVenue(int idVenue) {
        resetStarredState();
        return Realm.getDefaultInstance().where(Event.class)
                .equalTo("venue.id", idVenue)
                .findAllSorted(Event.DAY, Sort.ASCENDING, Event.TIME_HOUR_MIDNIGHT_SAFE, Sort.ASCENDING);
    }


    public static void resetStarredState() {

        Realm realm = Realm.getDefaultInstance();
        List<Event> events = realm.where(Event.class).findAll();

        realm.beginTransaction();
        for (Event event : events) {
            event.setStarred(isEventStarred(event.getId()));
        }
        realm.commitTransaction();
    }

    public void toggleFavState(final int idEvent) {

        Realm realm = Realm.getDefaultInstance();
        final Favourite favourite = realm.where(Favourite.class)
                .equalTo(Favourite.ID_EVENT, idEvent)
                .findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                if (favourite != null) {
                    favourite.setStarred(!favourite.isStarred());
                } else {
                    Favourite favouriteNew = realm.createObject(Favourite.class);
                    favouriteNew.setStarred(true);
                    favouriteNew.setIdEvent(idEvent);
                }
            }
        });

    }

//    private int getAutoincrementFavId() {
//
//        Realm realm = Realm.getDefaultInstance();
//        Number currentIdNum = realm.where(Favourite.class).max(Favourite.ID);
//        int nextId;
//        if (currentIdNum == null) {
//            nextId = 1;
//        } else {
//            nextId = currentIdNum.intValue() + 1;
//        }
//
//        return nextId;
//
//    }

    private static boolean isEventStarred(int idEvent) {

        Realm realm = Realm.getDefaultInstance();
        Favourite favourite = realm.where(Favourite.class)
                .equalTo(Favourite.ID_EVENT, idEvent)
                .findFirst();
        return favourite != null && favourite.isStarred();
    }


    private static Event getEventById(int idEvent) {
        return Realm.getDefaultInstance().where(Event.class).equalTo(Event.ID, idEvent).findFirst();
    }

    // Not used
//    public void getEvents(final EventsCallback callback) {
//
//        if (!Util.isConnected(context)) {
//            baseView.toast(R.string.no_connection);
//            return;
//        }
//
//        baseView.setRefresing(true);
//
//        getApi().getEvents()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnTerminate(actionTerminate)
//                .subscribe(new Observer<List<Event>>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                        callback.onError(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(List<Event> events) {
//
//                        baseView.setRefresing(false);
//
//                        callback.onResponse(events);
//
//
//                    }
//                });


//    }


    private Api getApi() {
        return getApi(Api.class);
    }


}
