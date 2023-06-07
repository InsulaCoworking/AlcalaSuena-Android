package com.triskelapps.alcalasuena.ui.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.BuildConfig;
import com.triskelapps.alcalasuena.DebugHelper;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.BandInteractor;
import com.triskelapps.alcalasuena.interactor.EventInteractor;
import com.triskelapps.alcalasuena.interactor.NewsInteractor;
import com.triskelapps.alcalasuena.interactor.SettingsInteractor;
import com.triskelapps.alcalasuena.interactor.VenueInteractor;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.Filter;
import com.triskelapps.alcalasuena.model.News;
import com.triskelapps.alcalasuena.model.Venue;
import com.triskelapps.alcalasuena.model.notification.FirebasePush;
import com.triskelapps.alcalasuena.ui.band_info.BandInfoPresenter;
import com.triskelapps.alcalasuena.ui.event_info.EventInfoPresenter;
import com.triskelapps.alcalasuena.ui.info.WebViewActivity;
import com.triskelapps.alcalasuena.ui.news_info.NewsInfoPresenter;
import com.triskelapps.alcalasuena.ui.splash.SplashPresenter;
import com.triskelapps.alcalasuena.ui.venue_info.VenueInfoPresenter;
import com.triskelapps.alcalasuena.util.DateUtils;
import com.triskelapps.alcalasuena.util.Util;
import com.triskelapps.alcalasuena.util.WebUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.nlopez.smartlocation.SmartLocation;

import static com.triskelapps.alcalasuena.App.SHARED_FIRST_TIME_APP_LAUNCHING;

/**
 * Created by julio on 23/05/17.
 */


public class MainPresenter extends BasePresenter {

    private static final String URL_QUERY_SHARE = "share";


    private final MainView view;
    private final BandInteractor bandInteractor;
    private final EventInteractor eventInteractor;

    public static List<String> tabsDays = App.festDates;

    private final SettingsInteractor settingsInteractor;
    private final NewsInteractor newsInteractor;
    private final VenueInteractor venueInteractor;

    private Filter filter;

    private BroadcastReceiver receiverRefreshData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(App.ACTION_REFRESH_DATA)) {
                refreshData();
//                view.toast("action refresh");
            }
        }
    };
    private Integer newDataVersion;
    private List<Event> events = new ArrayList<>();
    private boolean searchingLocation;
    private boolean autoTimeScroll;

    public static Intent newMainActivity(Context context) {

        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }

    public static MainPresenter newInstance(MainView view, Context context) {

        return new MainPresenter(view, context);

    }

    private MainPresenter(MainView view, Context context) {
        super(context, view);

        this.view = view;
        bandInteractor = new BandInteractor(context, view);
        venueInteractor = new VenueInteractor(context, null);
        eventInteractor = new EventInteractor(context, view);
        settingsInteractor = new SettingsInteractor(context, view);
        newsInteractor = new NewsInteractor(context, view);


    }

    public void onCreate(Intent intent) {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(App.ACTION_REFRESH_DATA);
        context.registerReceiver(receiverRefreshData, intentFilter);

        filter = new Filter();

        // During the festival go directly to current day
        Calendar midnightSafeCalendar = Calendar.getInstance();
        midnightSafeCalendar.add(Calendar.HOUR_OF_DAY, -5); // This way we considere "next day" after 5:00am

        String currentDay = Event.dateFormatApi.format(new Date(midnightSafeCalendar.getTimeInMillis()));
        int tabPosition = 0;
        if (tabsDays.contains(currentDay)) {
            tabPosition = tabsDays.indexOf(currentDay);
            view.setTabPosition(tabPosition);
        }

        filter.setDay(tabsDays.get(tabPosition));

        checkIntentUriReceived(intent);

        if (BuildConfig.MODE_PREPARING) {
            SplashPresenter.launchSplashActivity(context, SplashPresenter.NEXT_SCREEN_NONE);
            ((Activity) context).finish();
        } else if (getPrefs().getBoolean(SHARED_FIRST_TIME_APP_LAUNCHING, true) /*|| BuildConfig.DEBUG*/) {
            SplashPresenter.launchSplashActivity(context, SplashPresenter.NEXT_SCREEN_MAIN);
            getPrefs().edit().putBoolean(SHARED_FIRST_TIME_APP_LAUNCHING, false).commit();
        } else {
            view.checkNotificationsPermission();
        }

        checkSendNewsPermission();

        autoTimeScroll = true;

    }

    private void checkSendNewsPermission() {

        String pin = context.getString(R.string.pin_send_news);
        String deviceId = Util.getDeviceId(context);
        String hash = Util.getMD5Hash(pin + deviceId);
        String hashStored = getPrefs().getString(App.SHARED_PIN_SEND_NEWS_ENCRIPT, null);
        if (TextUtils.equals(hash, hashStored)) {
            view.showSendNewsButton();
        }
    }


    public void onResume() {

        refreshData();

        if (Util.isConnected(context)) {
            checkDataVersionAndUpdate();
        }
    }

    public void onStop() {
        if (searchingLocation) {
            onHappeningNowButtonClick();
        }
    }

    public void onDestroy() {
        context.unregisterReceiver(receiverRefreshData);
    }


    private void checkDataVersionAndUpdate() {

        settingsInteractor.getLastDataVersion(new SettingsInteractor.SettingsIntValueCallback() {
            @Override
            public void onResponse(Integer version) {
                int currentDataVersion = getPrefs().getInt(App.SHARED_CURRENT_DATA_VERSION, App.CACHED_DATA_VERSION);
                if (version > currentDataVersion || DebugHelper.SWITCH_FORZE_DATA_SYNC) {
                    newDataVersion = version;
                    updateBandsFromApi();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }


    private void updateBandsFromApi() {

        Log.i(TAG, "---> update start");
        bandInteractor.getBands(new BandInteractor.BandsCallback() {
            @Override
            public void onResponse(List<Band> bands) {

                updateVenuesFromApi();

            }

            @Override
            public void onError(String error) {
                FirebaseCrashlytics.getInstance().recordException(new Error("Error updating bands from API: " + error));
//                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "---> update bands error");
            }
        });
    }


    private void updateVenuesFromApi() {
        venueInteractor.getVenuesApi(new VenueInteractor.VenuesCallback() {
            @Override
            public void onResponse(List<Venue> venues) {

                sendUpdateDataBroadcast();

                Log.i(TAG, "---> update end");

                getPrefs().edit().putInt(App.SHARED_CURRENT_DATA_VERSION, newDataVersion).commit();
            }

            @Override
            public void onError(String error) {
                FirebaseCrashlytics.getInstance().recordException(new Error("Error updating venues from API: " + error));
//                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                Log.i(TAG, "---> update venues error");
            }
        });
    }

    private void sendUpdateDataBroadcast() {
        context.sendBroadcast(new Intent(App.ACTION_REFRESH_DATA));
    }


    public void refreshData() {

        events.clear();

        if (App.getDB().venueDao().getAll().isEmpty()) {
            view.showEvents(events, "");
            view.showEventDataNotPreparedView(true);
            return;
        } else {
            view.showEventDataNotPreparedView(false);
        }

        events.addAll(eventInteractor.getEventsDB(filter));

        String emptyMessage = null;
        if (events.isEmpty()) {
            if (filter.isStarred()) {
                emptyMessage = context.getString(R.string.no_favourites);
            } else {
                emptyMessage = context.getString(R.string.no_results_for_tags);
            }
        }

        view.showEvents(events, emptyMessage);

        // Little extra: scroll to events happening now
        if (autoTimeScroll) {
            try {
                if (isCurrentTabDay()) {
                    String hourNow = new SimpleDateFormat("HH").format(new Date());
                    for (int i = 0; i < events.size(); i++) {
                        Event event = events.get(i);
                        if (event.getTime().startsWith(hourNow)) {
                            view.goToEventsTakingPlaceNow(i);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                // Bad idea if this little extra make an app crash :S
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }

        autoTimeScroll = false;
    }


    private boolean isCurrentTabDay() {
        return filter.getDay().equals(Event.dateFormatApi.format(new Date()));
    }


    // --- INTERACTIONS
    public void onTabSelected(int position) {
        filter.setDay(tabsDays.get(position));
        refreshData();
        view.goToTop();
    }


    public void onFilterFavouritesClicked(boolean favourites) {
        filter.setStarred(favourites);
        refreshData();
    }

    public void onEventFavouriteClicked(int idEvent) {
        eventInteractor.toggleFavState(idEvent);
        refreshData();
    }

    public void onEventClick(Event event) {
        if (event.mustShowBandInfo()) {
            context.startActivity(BandInfoPresenter.newBandInfoActivity(context, event.getBands().get(0).getId()));
        } else {
            EventInfoPresenter.launchEventInfoActivity(context, event.getId());
        }
    }

    public void onShareFavsButtonClicked() {

        String text = getMyListTextToShare();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        context.startActivity(intent);
    }

    //------------

    private String getMyListTextToShare() {

        Filter filter = new Filter();
        filter.setStarred(true);
        List<Event> eventsFav = eventInteractor.getEventsDB(filter);
        String text = context.getString(R.string.share_favs_text_intro);

        String importLink = "https://alcalasuena.es/share/?" + URL_QUERY_SHARE + "=";

        for (Event eventFav : eventsFav) {
            text += "\n\n";
            text += eventFav.getBandsNames() + "\n";
            text += eventFav.getDayShareFormat() + " - " + eventFav.getTimeFormatted() + "\n";
            text += eventFav.getVenue().getName();

            importLink += eventFav.getId() + ",";
        }

        importLink = importLink.substring(0, importLink.length() - 1);
        text += "\n\n" + String.format(context.getString(R.string.import_link_text), importLink);
        text += "\n\n" + String.format(context.getString(R.string.download_app_text),
                App.URL_GOOGLE_PLAY_APP, App.URL_APPLE_STORE_APP);

        return text;
    }


    private void checkIntentUriReceived(Intent intent) {

        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {

            String idsFavEvents = appLinkData.getQueryParameter(URL_QUERY_SHARE);
            if (idsFavEvents != null) {
                String[] idsFavs = idsFavEvents.split(",");
                showImportEventsDialog(idsFavs);
            } else {
                // This link is not for this app
                showFunnyDialogForThisUserFail(appLinkData);
            }
        } else if (intent.hasExtra(FirebasePush.NOTIFICATION_NEWS)) {

            String newsJson = intent.getStringExtra(FirebasePush.NOTIFICATION_NEWS);
            final News news = new Gson().fromJson(newsJson, News.class);
            if (news != null) {
                news.configureDatesTime();
                int newsId = news.getId();
                newsInteractor.storeNewsIndividual(news);
                context.startActivity(NewsInfoPresenter.newNewsInfoActivity(context, newsId));

            }
        }

        if (intent.hasExtra(FirebasePush.EXTRA_OPEN_URL_LINK)) {
            String link = intent.getStringExtra(FirebasePush.NOTIFICATION_CUSTOM_BUTTON_LINK);
            WebViewActivity.startRemoteUrl(context, null, link);
        }
    }


    private void showImportEventsDialog(final String[] idsFavsStr) {

        final Integer[] idsFavsEvents = new Integer[idsFavsStr.length];
        for (int i = 0; i < idsFavsStr.length; i++) {
            String idStr = idsFavsStr[i];
            idsFavsEvents[i] = Integer.parseInt(idStr);
        }

        List<Event> eventsFav = eventInteractor.getEventsFavsDB(idsFavsEvents);
        String text = "";

        for (Event eventFav : eventsFav) {
            text += "\n\n";
            text += eventFav.getBandsNames() + "\n";
            text += eventFav.getDayShareFormat() + " - " + eventFav.getTimeFormatted() + "\n";
            text += eventFav.getVenue().getName();
        }

        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(R.string.add_favs_events);
        ab.setMessage(text);
        ab.setPositiveButton(R.string.add, (dialog, which) -> {
            eventInteractor.setFavEvents(idsFavsEvents);
            refreshData();
        });
        ab.setNegativeButton(R.string.no_thanks, null);
        ab.show();
    }


    private void showFunnyDialogForThisUserFail(final Uri appLinkData) {

        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(R.string.hello_excl);
        ab.setMessage(R.string.funny_text_user_enter_with_wrong_link);
        ab.setPositiveButton(R.string.follow_link, (dialog, which) -> {
            WebUtils.openCustomTab(context, appLinkData.toString());
        });
        ab.setNeutralButton(R.string.stay_in_app, null);
        ab.show();
    }

    public String getWeekDayForTabPosition(int position) {

        DateFormat weekDayDF = new SimpleDateFormat("EEE");

        try {
            Date date = DateUtils.formatDateApi.parse(tabsDays.get(position));
            return weekDayDF.format(date).toUpperCase().replace(".", "");
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalStateException("Date format invalid");
        }
    }

    public String getDayMonthForTabPosition(int position) {

        DateFormat dayMonthDF = new SimpleDateFormat("d MMMM");

        try {
            Date date = DateUtils.formatDateApi.parse(tabsDays.get(position));
            return dayMonthDF.format(date).toLowerCase();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalStateException("Date format invalid");
        }
    }

    public void onHappeningNowButtonClick() {

        searchingLocation = !searchingLocation;

        if (!BuildConfig.DEBUG) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "onHappeningNowButtonClick. " + (searchingLocation ? "START" : "STOP"));
            FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, bundle);
        }

        if (searchingLocation) {

            SmartLocation.with(context).location().oneFix().start(location -> {
//                view.toast("encontrado: precisión: " + location.getAccuracy());
                if (searchingLocation) {
                    // to stop location search and refresh ui
                    onHappeningNowButtonClick();
                }

                if (location.getAccuracy() > Event.MIN_ACCURACY_LOCATION_HAPPENING_NOW) {
                    view.toast(R.string.accuracy_enought_location_not_found);
                    return;
                }

                Venue venue = searchClosestVenue(location);
                if (venue != null) {
//                    view.toast("Escenario cercano: " + venue.getName());
                    context.startActivity(VenueInfoPresenter.newVenueInfoActivity(context, venue.getId(), true));
                }
            });
        } else {
            SmartLocation.with(context).location().stop();
        }

        view.showProgressHappeningNow(searchingLocation);


    }

    private Venue searchClosestVenue(Location location) {
        List<Venue> venues = App.getDB().venueDao().getAll();
        double closestDistance = Double.MAX_VALUE;
        int indexVenueClosestDistance = -1;
        for (int i = 0; i < venues.size(); i++) {
            Venue venue = venues.get(i);
            double distanceKm = calculateDistance(location.getLatitude(), location.getLongitude(),
                    venue.getLatitude(), venue.getLongitude(), "K");
            double distanceMeters = distanceKm * 1000;
            if (distanceMeters < closestDistance) {
                closestDistance = distanceMeters;
                indexVenueClosestDistance = i;
            }
        }

        if (indexVenueClosestDistance == -1) {
            view.toast(R.string.error);
            return null;
        }

        if (closestDistance > Event.MIN_DISTANCE_TO_VENUE_HAPPENING_NOW) {
            view.toast(R.string.not_close_to_venue);
            return null;
        }

        return venues.get(indexVenueClosestDistance);
    }


    // https://www.geodatasource.com/developers/java
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }
}
