package com.triskelapps.alcalasuena.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.google.firebase.crash.FirebaseCrash;
import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.BuildConfig;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.BandInteractor;
import com.triskelapps.alcalasuena.interactor.EventInteractor;
import com.triskelapps.alcalasuena.interactor.NewsInteractor;
import com.triskelapps.alcalasuena.interactor.SettingsInteractor;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.Filter;
import com.triskelapps.alcalasuena.model.News;
import com.triskelapps.alcalasuena.ui.band_info.BandInfoPresenter;
import com.triskelapps.alcalasuena.views.DialogShowNews;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.triskelapps.alcalasuena.App.EXTRA_FIRST_TIME_APP_LAUNCHING;

/**
 * Created by julio on 23/05/17.
 */


public class MainPresenter extends BasePresenter {

    private static final String URL_QUERY_SHARE = "share";
    public static final String URL_GOOGLE_PLAY_APP = "https://play.google.com/store/apps/details?id=com.triskelapps.alcalasuena";
    public static final String URL_DIRECT_GOOGLE_PLAY_APP = "market://details?id=com.triskelapps.alcalasuena";


    private final MainView view;
    private final BandInteractor bandInteractor;
    private final EventInteractor eventInteractor;

    private static List<String> tabsDays = new ArrayList<>();

    static {
        tabsDays.add("2017-06-02");
        tabsDays.add("2017-06-03");
        tabsDays.add("2017-06-04");
    }

    private final SettingsInteractor settingsInteractor;
    private final NewsInteractor newsInteractor;

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
        eventInteractor = new EventInteractor(context, view);
        settingsInteractor = new SettingsInteractor(context, view);
        newsInteractor = new NewsInteractor(context, view);

        getPrefs().edit().putBoolean(EXTRA_FIRST_TIME_APP_LAUNCHING, false).commit();

    }

    public void onCreate(Intent intent) {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(App.ACTION_REFRESH_DATA);
        context.registerReceiver(receiverRefreshData, intentFilter);

        filter = new Filter();

        // During the festival go directly to current day
        String currentDay = Event.dateFormatApi.format(new Date());
        int tabPosition = 0;
        if (tabsDays.contains(currentDay)) {
            tabPosition = tabsDays.indexOf(currentDay);
            view.setTabPosition(tabPosition);
        }

        filter.setDay(tabsDays.get(tabPosition));

        checkIntentUriReceived(intent);

        checkNewVersionInMarket();
        checkNews();

    }


    public void onResume() {

        refreshData();
    }

    public void onDestroy() {
        context.unregisterReceiver(receiverRefreshData);
    }

    public void refreshData() {

        List<Event> events = eventInteractor.getEventsDB(filter);

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
            FirebaseCrash.report(e);
        }

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
        eventInteractor.toggleFavState(idEvent, false);
        refreshData();
    }

    public void onBandClicked(int idBand) {
        context.startActivity(BandInfoPresenter.newBandInfoActivity(context, idBand));
    }

    public void onShareFavsButtonClicked() {

        String text = getMyListTextToShare();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        context.startActivity(intent);
    }


    public void onUpdateVersionClick() {

        Intent directPlayIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_DIRECT_GOOGLE_PLAY_APP));
        if (directPlayIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(directPlayIntent);
        } else {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_GOOGLE_PLAY_APP)));
        }
    }


    //------------

    private String getMyListTextToShare() {

        Filter filter = new Filter();
        filter.setStarred(true);
        List<Event> eventsFav = eventInteractor.getEventsDB(filter);
        String text = context.getString(R.string.share_favs_text_intro);

        String importLink = "http://www.alcalasuena.es/?" + URL_QUERY_SHARE + "=";

        for (Event eventFav : eventsFav) {
            text += "\n\n";
            text += eventFav.getBandEntity().getName() + "\n";
            text += eventFav.getDayShareFormat() + " - " + eventFav.getTimeFormatted() + "\n";
            text += eventFav.getVenue().getName();

            importLink += eventFav.getId() + ",";
        }

        importLink = importLink.substring(0, importLink.length() - 1);
        text += "\n\n" + String.format(context.getString(R.string.import_link_text), importLink);
        text += "\n\n" + String.format(context.getString(R.string.download_app_text), URL_GOOGLE_PLAY_APP);

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


        }
    }


    private void checkNewVersionInMarket() {
        settingsInteractor.getAppVersionInMarket(new SettingsInteractor.SettingsAppVersionCallback() {
            @Override
            public void onResponse(Integer newVersion) {
                int currentVersion = BuildConfig.VERSION_CODE;
                if (newVersion > currentVersion) {
                    if (!BuildConfig.DEBUG) {
                        view.showNewVersionAvailable();
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }


    private void checkNews() {

        newsInteractor.getNewsFromApi(new NewsInteractor.NewsCallback() {
            @Override
            public void onResponse(List<News> newsList) {
                News news = newsInteractor.getLastUnseenNews();
                if (news != null) {
                    DialogShowNews.newInstace(context).show(news);
                    newsInteractor.setNewsSeen(news.getId());
                }
            }

            @Override
            public void onError(String error) {

            }
        });
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
            text += eventFav.getBandEntity().getName() + "\n";
            text += eventFav.getDayShareFormat() + " - " + eventFav.getTimeFormatted() + "\n";
            text += eventFav.getVenue().getName();
        }

        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(R.string.add_favs_events);
        ab.setMessage(text);
        ab.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eventInteractor.setFavEvents(idsFavsEvents);
                refreshData();
            }
        });
        ab.setNegativeButton(R.string.no_thanks, null);
        ab.show();
    }


    private void showFunnyDialogForThisUserFail(final Uri appLinkData) {

        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(R.string.hello_excl);
        ab.setMessage(R.string.funny_text_user_enter_with_wrong_link);
        ab.setPositiveButton(R.string.follow_link, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, appLinkData));
            }
        });
        ab.setNeutralButton(R.string.stay_in_app, null);
        ab.show();
    }


}
