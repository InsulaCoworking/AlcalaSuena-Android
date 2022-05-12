package com.triskelapps.alcalasuena.database.dao;

import android.text.TextUtils;

import androidx.room.Dao;
import androidx.room.Query;

import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.interactor.EventInteractor;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Dao
public abstract class EventDao implements BaseDao<Event> {


    @Query("SELECT * FROM event")
    protected abstract List<Event> getAll();

    public List<Event> getAllFull() {
        List<Event> events = getAll();
        addBandsAndVenue(events);
        return events;
    }


    @Query("SELECT * FROM event WHERE id = :id")
    protected abstract Event getEventById(int id);

    public Event getEventByIdFull(int id) {
        return addBandsAndVenue(getEventById(id));
    }


    @Query("SELECT * FROM event WHERE id IN (:ids)")
    protected abstract List<Event> getEventsByIds(Integer[] ids);

    public List<Event> getEventsByIdsFull(Integer[] ids) {
        return addBandsAndVenue(getEventsByIds(ids));
    }


    @Query("SELECT * FROM event WHERE day = :day ORDER BY day, timeHourMidnightSafe")
    protected abstract List<Event> getEventsForDay(String day);

    public List<Event> getEventsForDayFull(String day) {
        return addBandsAndVenue(getEventsForDay(day));
    }


    @Query("SELECT * FROM event " +
            "WHERE id IN (SELECT idEvent FROM favourite WHERE starred = 1) " +
            "ORDER BY day, timeHourMidnightSafe")
    protected abstract List<Event> getEventsStarred();

    public List<Event> getEventsForDayStarredFull(String day) {
        List<Event> eventsStarred = getEventsStarred();
        if (day != null) {
            eventsStarred = eventsStarred.stream().filter(event ->
                    TextUtils.equals(day, event.getDay())).collect(Collectors.toList());
        }
        return addBandsAndVenue(eventsStarred);
    }


    @Query("SELECT * FROM event WHERE idVenue = :idVenue ORDER BY day, timeHourMidnightSafe")
    protected abstract List<Event> getEventsForVenue(int idVenue);

    public List<Event> getEventsForVenueFull(int idVenue) {
        return addBandsAndVenue(getEventsForVenue(idVenue));
    }

//    @Query("UPDATE event " +
//            "SET starred = " +
//            "(SELECT starred FROM favourite WHERE event.id = favourite.idEvent)")
//    void updateStarredEvents();

    @Query("DELETE FROM event")
    public abstract void deleteAll();



    private List<Event> addBandsAndVenue(List<Event> events) {
        events.stream().forEach(this::addBandsAndVenue);
        return events;
    }

    private Event addBandsAndVenue(Event event) {
        addBandsToEvent(event);
        event.setVenue(App.getDB().venueDao().getVenueById(event.getIdVenue()));
        event.setStarred(App.getDB().favouriteDao().isStarred(event.getId()));
        return event;
    }

    public void addBandsToEvent(Event event) {
        String[] idsStr = event.getBandsIdsStr().split(",");
        Arrays.stream(idsStr).forEach(idStr -> event.addBand(App.getDB().bandDao().getBandById(Integer.parseInt(idStr))));
    }

}
