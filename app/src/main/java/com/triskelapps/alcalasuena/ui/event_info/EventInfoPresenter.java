package com.triskelapps.alcalasuena.ui.event_info;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.EventInteractor;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.ui.band_info.BandInfoPresenter;

public class EventInfoPresenter extends BasePresenter {

    private static final String EXTRA_EVENT_ID = "extra_event";
    private final EventInfoView view;
    private final EventInteractor eventInteractor;
    private Event event;
    private int eventId;

    public static void launchEventInfoActivity(Context context, int eventId) {

        Intent intent = new Intent(context, EventInfoActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        context.startActivity(intent);
    }

    public static EventInfoPresenter newInstance(EventInfoView view, Context context) {

        return new EventInfoPresenter(view, context);

    }

    private EventInfoPresenter(EventInfoView view, Context context) {
        super(context, view);

        this.view = view;

        eventInteractor = new EventInteractor(context, view);

    }

    public void onCreate(Intent intent) {

        eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1);
        if (eventId == -1) {
            throw new IllegalArgumentException("wrong event id");
        }

        refreshData();


    }

    public void onResume() {

    }

    public void refreshData() {

        event = EventInteractor.getEventById(eventId);
        view.showEventInfo(event);

    }

    public void onBuyTicketsClick() {
        if (event != null) {
            openCustomTab(event.getTicketsUrl());
        }
    }

    private void openCustomTab(String url) {

        CustomTabColorSchemeParams params = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .build();

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
                .setDefaultColorSchemeParams(params)
                .build();

        try {
            customTabsIntent.launchUrl(context, Uri.parse(url));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }

    }

    public void onBandClick(int id) {
        context.startActivity(BandInfoPresenter.newBandInfoActivity(context, id));
    }

    public void onEventFavouriteClicked(int idEvent) {
        eventInteractor.toggleFavState(idEvent, false);
        refreshData();
    }
}
