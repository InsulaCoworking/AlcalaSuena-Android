package com.triskelapps.alcalasuena.ui.map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.VenueInteractor;
import com.triskelapps.alcalasuena.model.Venue;
import com.triskelapps.alcalasuena.ui.venue_info.VenueInfoPresenter;

import java.util.List;

import io.nlopez.smartlocation.SmartLocation;


/**
 * Created by julio on 28/05/17.
 */


public class MapPresenter extends BasePresenter {

    private final MapView view;
    private final VenueInteractor venuesInteractor;
    private Location mLastLocation;
    private Venue lastVenueSelected;

    public static Intent newMapActivity(Context context) {

        Intent intent = new Intent(context, MapActivity.class);

        return intent;
    }

    public static MapPresenter newInstance(MapView view, Context context) {

        return new MapPresenter(view, context);

    }

    private MapPresenter(MapView view, Context context) {
        super(context, view);

        this.view = view;
        venuesInteractor = new VenueInteractor(context, view);

    }

    public void onCreate() {

    }


    public void onResume() {

    }

    public void onMapReady() {
        refreshData();
    }

    public void refreshData() {

        List<Venue> venues = venuesInteractor.getVenuesDB();
        view.showVenues(venues);

    }


    public void onVenuesMarkerClick(Venue venue) {
        lastVenueSelected = venue;
        view.showVenueInfo(venue);
    }

    public void onMapClick() {
        lastVenueSelected = null;
        view.hideVenueInfo();
    }

    public void onVenueMoreInfoClick() {

        context.startActivity(VenueInfoPresenter.newVenueInfoActivity(context, lastVenueSelected.getId()));
    }

    public void onVenueIndicationsClick() {
        Location location = SmartLocation.with(context).location().getLastLocation();

        String destinationCoords = lastVenueSelected.getLatitude() + "," + lastVenueSelected.getLongitude();

        String uri = null;
        if (location != null) {

            String modeWalk = "!4m2!4m1!3e2";
            String originCoords = location.getLatitude() + "," + location.getLongitude();
            uri = "https://www.google.com/maps/dir/" + originCoords + "/" + destinationCoords + "/data=" + modeWalk;
        } else {
            uri = "geo:0,0?q=" + destinationCoords + " (" + lastVenueSelected.getName() + ")";

        }

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        } else {
            view.toast(R.string.no_googlemaps_available);
        }

    }

}
