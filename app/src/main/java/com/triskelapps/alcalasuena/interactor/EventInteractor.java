package com.triskelapps.alcalasuena.interactor;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.api.Api;
import com.triskelapps.alcalasuena.base.BaseInteractor;
import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.Favourite;
import com.triskelapps.alcalasuena.model.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by julio on 14/02/16.
 */
public class EventInteractor extends BaseInteractor {


    public EventInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }


    public List<Event> getEventsDB(Filter filter) {

        if (filter == null) {
            return App.getDB().eventDao().getAllFull();
        } else {
            if (filter.isStarred()) {
                return App.getDB().eventDao().getEventsForDayStarredFull(filter.getDay());
            } else {
                List<Event> events = App.getDB().eventDao().getEventsForDayFull(filter.getDay());
                return removeEventsWithNoTagsActive(events);
            }
        }
    }

    public boolean hasEvents() {
        return !getEventsDB(null).isEmpty();
    }

    private List<Event> removeEventsWithNoTagsActive(List<Event> events) {

        return events.stream().filter(event ->
                        event.getBands() != null && event.getBands().stream().anyMatch(band ->
                                App.getDB().tagStateDao().isTagActive(band.getIdTag())))
                .collect(Collectors.toList());

    }


    public List<Event> getEventsForBand(int idBand) {

        List<Event> events = App.getDB().eventDao().getAllFull();

        return events.stream().filter(event ->
                event.getBands() != null &&
                event.getBands().stream().anyMatch(band ->
                        band.getId() == idBand))
                .collect(Collectors.toList());

    }


    public List<Event> getEventsForVenue(int idVenue) {
        List<Event> events = App.getDB().eventDao().getEventsForVenueFull(idVenue);
        return events;
    }


    public void toggleFavState(final int idEvent) {

        final Favourite favourite = App.getDB().favouriteDao().getFavouriteEvent(idEvent);

        if (favourite != null) {
            favourite.setStarred(!favourite.isStarred());
            App.getDB().favouriteDao().update(favourite);
        } else {
            Favourite favouriteNew = new Favourite();
            favouriteNew.setStarred(true);
            favouriteNew.setIdEvent(idEvent);
            App.getDB().favouriteDao().insert(favouriteNew);
        }

        Event event = getEventById(idEvent);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, event.getId() + "");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, event.getBandsNames());
        bundle.putString(FirebaseAnalytics.Param.SCORE, favourite == null || !favourite.isStarred() ? "1" : "-1"); // is toggling
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, bundle);


    }


    public void setFavEvents(Integer[] idsFavs) {
        App.getDB().favouriteDao().setEventsStarred(idsFavs);
    }

    public static Event getEventById(int idEvent) {
        Event event = App.getDB().eventDao().getEventByIdFull(idEvent);
        return event;
    }


    public List<Event> getEventsFavsDB(Integer[] idsFavsEvents) {
        List<Event> events = App.getDB().eventDao().getEventsByIdsFull(idsFavsEvents);
        return events;
    }

    private Api getApi() {
        return getApi(Api.class);
    }


}
