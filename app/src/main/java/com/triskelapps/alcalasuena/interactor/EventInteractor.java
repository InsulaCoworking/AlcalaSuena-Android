package com.triskelapps.alcalasuena.interactor;

import android.content.Context;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.api.Api;
import com.triskelapps.alcalasuena.base.BaseInteractor;
import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.Tag;
import com.triskelapps.alcalasuena.util.Util;

import java.util.List;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class EventInteractor extends BaseInteractor {



    public interface EventsCallback {

        void onResponse(List<Event> events);

        void onError(String error);
    }

    public EventInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }

    public static void initializeEvents() {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                try {
                    Tag tag1 = realm.createObject(Tag.class, 1);
                    tag1.setName("Jazz");

                    Tag tag2 = realm.createObject(Tag.class, 2);
                    tag2.setName("Blues");


//                    Event event1 = realm.createObject(Event.class, 1);
//                    event1.setName("Los Pepes");
//                    event1.setTag(tag1);
//
//
//                    Event event2 = realm.createObject(Event.class, 2);
//                    event2.setName("Las Juanas");
//                    event2.setTag(tag2);

                } catch (RealmPrimaryKeyConstraintException e) {

                }

            }
        });

    }


    public List<Event> getEventsDB() {
        return Realm.getDefaultInstance().where(Event.class).findAll();
    }

    public void getEvents(final EventsCallback callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        baseView.setRefresing(true);

        getApi().getEvents()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(actionTerminate)
                .subscribe(new Observer<List<Event>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Event> events) {

                        baseView.setRefresing(false);

                        callback.onResponse(events);


                    }
                });


    }



    private Api getApi() {
        return getApi(Api.class);
    }


}
