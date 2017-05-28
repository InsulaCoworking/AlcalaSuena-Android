package com.triskelapps.alcalasuena.ui.venues;

import android.content.Context;
import android.content.Intent;

import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.VenueInteractor;
import com.triskelapps.alcalasuena.model.Venue;

import java.util.List;

/**
 * Created by julio on 23/05/17.
 */


public class VenuesPresenter extends BasePresenter {

    private final VenuesView view;
    private final VenueInteractor venueInteractor;

    public static Intent newVenuesActivity(Context context) {

        Intent intent = new Intent(context, VenuesActivity.class);

        return intent;
    }

    public static VenuesPresenter newInstance(VenuesView view, Context context) {

        return new VenuesPresenter(view, context);

    }

    private VenuesPresenter(VenuesView view, Context context) {
        super(context, view);

        this.view = view;
        venueInteractor = new VenueInteractor(context, view);

    }

    public void onCreate() {

    }


    public void onResume() {

        refreshData();
    }

    public void refreshData() {

        List<Venue> venues = venueInteractor.getVenuesDB();
        view.showVenues(venues);

    }


    public void onVenueClicked(int idVenue) {
//        context.startActivity(VenueInfoPresenter.newVenueInfoActivity(context, idVenue));
        view.toast("venue clicked: " + idVenue);
    }
}
