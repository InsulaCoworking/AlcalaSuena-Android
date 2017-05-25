package com.triskelapps.alcalasuena.ui;

import android.content.Context;
import android.content.Intent;

import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.BandInteractor;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julio on 23/05/17.
 */


 public class MainPresenter extends BasePresenter {

     private final MainView view;
    private final BandInteractor bandInteractor;

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

     }

     public void onCreate() {

         if (mustUpdateData()) {
             updateDataFromApi();
         }
     }

    private boolean mustUpdateData() {
        // TODO check last update date with server
        return true;
    }

    private void updateDataFromApi() {
        bandInteractor.getBands(new BandInteractor.BandsCallback() {
            @Override
            public void onResponse(List<Band> bands) {
                refreshData();
            }

            @Override
            public void onError(String error) {
                view.toast(error);
            }
        });
    }

    public void onResume() {

         refreshData();
     }

     public void refreshData() {

         List<Band> bands = bandInteractor.getBandsDB();
         List<Event> events = new ArrayList<>();

         for (Band band : bands) {
             Event event = new Event();
             event.setBand(band);
             events.add(event);
         }
         view.showEvents(events);

     }


}
