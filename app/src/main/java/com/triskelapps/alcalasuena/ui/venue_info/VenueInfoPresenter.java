package com.triskelapps.alcalasuena.ui.venue_info;

import android.content.Context;
import android.content.Intent;

import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.VenueInteractor;
import com.triskelapps.alcalasuena.model.Venue;

/**
 * Created by julio on 28/05/17.
 */


 public class VenueInfoPresenter extends BasePresenter {

    private static final String EXTRA_VENUE_ID = "extra_venue_id";

    private final VenueInfoView view;
    private final VenueInteractor venueInteractor;
    private int idVenue;

    public static Intent newVenueInfoActivity(Context context, int idVenue) {

         Intent intent = new Intent(context, VenueInfoActivity.class);
         intent.putExtra(EXTRA_VENUE_ID, idVenue);
         return intent;
     }

     public static VenueInfoPresenter newInstance(VenueInfoView view, Context context) {

         return new VenueInfoPresenter(view, context);

     }

     private VenueInfoPresenter(VenueInfoView view, Context context) {
         super(context, view);

         this.view = view;

         venueInteractor = new VenueInteractor(context, view);

     }

     public void onCreate(Intent intent) {

         idVenue = intent.getIntExtra(EXTRA_VENUE_ID, -1);
         if (idVenue == -1) {
             throw new IllegalArgumentException("No idVenue passed");
         }


     }

     public void onResume() {

         refreshData();
     }

     public void refreshData() {

         Venue venue = venueInteractor.getVenue(idVenue);
         view.showVenueInfo(venue);

     }

 }
