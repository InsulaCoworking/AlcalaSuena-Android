package com.triskelapps.alcalasuena.ui.intro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.BandInteractor;
import com.triskelapps.alcalasuena.interactor.EventInteractor;
import com.triskelapps.alcalasuena.interactor.VenueInteractor;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.Tag;
import com.triskelapps.alcalasuena.model.Venue;
import com.triskelapps.alcalasuena.ui.MainPresenter;

import java.util.List;

import io.realm.Realm;

/**
 * Created by julio on 26/05/17.
 */


 public class IntroPresenter extends BasePresenter {

     private final IntroView view;
    private final BandInteractor bandInteractor;
    private final VenueInteractor venueInteractor;

    public static Intent newIntroActivity(Context context) {

         Intent intent = new Intent(context, IntroActivity.class);

         return intent;
     }

     public static IntroPresenter newInstance(IntroView view, Context context) {

         return new IntroPresenter(view, context);

     }

     private IntroPresenter(IntroView view, Context context) {
         super(context, view);

         this.view = view;
         bandInteractor = new BandInteractor(context, view);
         venueInteractor = new VenueInteractor(context, view);

     }

    public void onCreate() {

        if (mustUpdateData()) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.where(Band.class).findAll().deleteAllFromRealm();
            realm.where(Event.class).findAll().deleteAllFromRealm();
            realm.where(Venue.class).findAll().deleteAllFromRealm();
            realm.where(Tag.class).findAll().deleteAllFromRealm();
            realm.commitTransaction();
            updateBandsFromApi();
        }
    }

    private boolean mustUpdateData() {
        // TODO check last update date with server
        return true;
    }

    private void updateBandsFromApi() {
        bandInteractor.getBands(new BandInteractor.BandsCallback() {
            @Override
            public void onResponse(List<Band> bands) {

                updateVenuesFromApi();
            }

            @Override
            public void onError(String error) {

                view.toast(error);
            }
        });
    }

    private void updateVenuesFromApi() {
        venueInteractor.getVenuesApi(new VenueInteractor.VenuesCallback() {
            @Override
            public void onResponse(List<Venue> venues) {
                EventInteractor.resetStarredState();
                lauchMainActivity();
            }

            @Override
            public void onError(String error) {
                view.toast(error);

            }
        });
    }

    private void lauchMainActivity() {
        context.startActivity(MainPresenter.newMainActivity(context));
        ((Activity)context).finish();
    }


 }
