package com.triskelapps.alcalasuena.ui;

import android.content.Context;
import android.content.Intent;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.BandInteractor;
import com.triskelapps.alcalasuena.interactor.EventInteractor;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.Filter;
import com.triskelapps.alcalasuena.ui.band_info.BandInfoPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julio on 23/05/17.
 */


 public class MainPresenter extends BasePresenter {

     private final MainView view;
    private final BandInteractor bandInteractor;
    private final EventInteractor eventInteractor;

    private static List<String> tabsDays = new ArrayList<>();
    static {
        tabsDays.add("2017-06-02");
        tabsDays.add("2017-06-03");
        tabsDays.add("2017-06-04");
    }

    private Filter filter;

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

     }

     public void onCreate() {

         filter = new Filter();
         filter.setDay(tabsDays.get(0));
         refreshData();
     }

    public void onResume() {

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

     }

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

    private String getMyListTextToShare() {

        Filter filter = new Filter();
        filter.setStarred(true);
        List<Event> eventsFav = eventInteractor.getEventsDB(filter);
        String text = context.getString(R.string.share_favs_text_intro);

        for (Event eventFav : eventsFav) {
            text += "\n\n";
            text += eventFav.getBandEntity().getName() + "\n";
            text += eventFav.getDayShareFormat() + " - " + eventFav.getTimeFormatted() + "\n";
            text += eventFav.getVenue().getName();
        }
        return text;
    }
}
