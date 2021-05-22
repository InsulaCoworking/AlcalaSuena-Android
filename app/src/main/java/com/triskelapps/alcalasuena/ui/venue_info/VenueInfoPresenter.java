package com.triskelapps.alcalasuena.ui.venue_info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.triskelapps.alcalasuena.BuildConfig;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.EventInteractor;
import com.triskelapps.alcalasuena.interactor.VenueInteractor;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.Venue;
import com.triskelapps.alcalasuena.ui.band_info.BandInfoPresenter;
import com.triskelapps.alcalasuena.ui.event_info.EventInfoPresenter;
import com.triskelapps.alcalasuena.util.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by julio on 28/05/17.
 */


public class VenueInfoPresenter extends BasePresenter {

    private static final String EXTRA_VENUE_ID = "extra_venue_id";
    private static final String EXTRA_FROM_HAPPENING_NOW = "extra_from_happening_now";

    private final VenueInfoView view;
    private final VenueInteractor venueInteractor;
    private final EventInteractor eventInteractor;
    private int idVenue;
    private boolean fromHappeningNow;
    private Venue venue;

    public static Intent newVenueInfoActivity(Context context, int idVenue) {

        Intent intent = new Intent(context, VenueInfoActivity.class);
        intent.putExtra(EXTRA_VENUE_ID, idVenue);
        return intent;
    }

    public static Intent newVenueInfoActivity(Context context, int idVenue, boolean fromHappeningNow) {

        Intent intent = new Intent(context, VenueInfoActivity.class);
        intent.putExtra(EXTRA_VENUE_ID, idVenue);
        intent.putExtra(EXTRA_FROM_HAPPENING_NOW, fromHappeningNow);
        return intent;
    }

    public static VenueInfoPresenter newInstance(VenueInfoView view, Context context) {

        return new VenueInfoPresenter(view, context);

    }

    private VenueInfoPresenter(VenueInfoView view, Context context) {
        super(context, view);

        this.view = view;

        venueInteractor = new VenueInteractor(context, view);
        eventInteractor = new EventInteractor(context, view);

    }

    public void onCreate(Intent intent) {

        idVenue = intent.getIntExtra(EXTRA_VENUE_ID, -1);
        if (idVenue == -1) {
            throw new IllegalArgumentException("No idVenue passed");
        }

        venue = venueInteractor.getVenue(idVenue);
        refreshData();

        fromHappeningNow = intent.getBooleanExtra(EXTRA_FROM_HAPPENING_NOW, false);
        if (fromHappeningNow) {
            view.selectEventsView();
        }

    }

    public void onResume() {

    }

    public void refreshData() {

        List<Event> eventsVenue = eventInteractor.getEventsForVenue(idVenue);
        int indexNextEventFromNow = getIndexNextEventFromNow(eventsVenue);
        view.showVenueInfo(venue, eventsVenue, indexNextEventFromNow);

    }

    private int getIndexNextEventFromNow(List<Event> eventsVenue) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < eventsVenue.size(); i++) {
            Event event = eventsVenue.get(i);

            // This must return previous event from which is the first after current time
            try {
                String datetimeEvent = event.getDay() + " " + event.getTime();
                Date eventDate = dateFormat.parse(datetimeEvent);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(eventDate);
                int hourOfDayEvent = calendar.get(Calendar.HOUR_OF_DAY);
                if (hourOfDayEvent > 0 && hourOfDayEvent < Event.TIME_HOUR_MIDNIGHT_SAFE_THRESHOLD) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                eventDate.setTime(calendar.getTimeInMillis());

                // todo finish this. fix case when events in new day have not started yet
                // for the moment it's enought doing only scroll and not highlighting row

//                Date eventDate = new Date(event.getTimeHourMidnightSafe());
                Date currentDate = BuildConfig.MOCK_CURRENT_DATETIME ? dateFormat.parse("2019-06-08 21:30:00") : new Date();
                if (eventDate.after(currentDate)) {
                    registerEventFound(event);
                    return i - 1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return 0;
    }

    private void registerEventFound(Event event) {

        if (BuildConfig.MOCK_CURRENT_DATETIME) {
            return;
        }

        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, event.getId() + "");
            bundle.putString(FirebaseAnalytics.Param.LEVEL_NAME, venue.getName());
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, event.getBandsNames());
            bundle.putString(FirebaseAnalytics.Param.START_DATE, Util.getCurrentDateTime());
            FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, bundle);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    public void onEventFavouriteClicked(int idEvent) {
        eventInteractor.toggleFavState(idEvent, false);
//        refreshData();
    }

    public void onEventClick(Event event) {
        EventInfoPresenter.launchEventInfoActivity(context, event.getId());
    }
}
