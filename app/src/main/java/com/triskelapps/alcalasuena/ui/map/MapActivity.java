package com.triskelapps.alcalasuena.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.Venue;

import java.util.List;

public class MapActivity extends BaseActivity implements MapView, View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private MapPresenter presenter;

    private final double latNorthAlcala = 40.483310;
    private final double latSouthAlcala = 40.478863;
    private final double lngEastAlcala = -3.360175;
    private final double lngWestAlcala = -3.374004;

    private LatLng positionAlcalaCenter = new LatLng(40.481534, -3.366189);

    private RelativeLayout viewVenueInfo;
    private ImageView imgVenue;
    private TextView tvVenueName;
    private TextView tvVenueMoreInfo;
    private TextView tvVenueIndications;
    private TextView tvVenueDescription;
    private GoogleMap map;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-28 19:01:18 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        viewVenueInfo = (RelativeLayout) findViewById(R.id.view_venue_info);
        imgVenue = (ImageView) findViewById(R.id.img_venue);
        tvVenueName = (TextView) findViewById(R.id.tv_venue_name);
        tvVenueDescription = (TextView) findViewById(R.id.tv_venue_description);
        tvVenueMoreInfo = (TextView) findViewById(R.id.tv_venue_more_info);
        tvVenueIndications = (TextView) findViewById(R.id.tv_venue_indications);

        tvVenueMoreInfo.setOnClickListener(this);
        tvVenueIndications.setOnClickListener(this);
    }


    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = MapPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        configureSecondLevelActivity();
        findViews();

        configureMap();

        presenter.onCreate();

    }


    private void checkLocationPermission() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        enableMyPositionInMaps();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        toast(R.string.location_wont_show);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, final PermissionToken token) {
                        new AlertDialog.Builder(MapActivity.this)
                                .setTitle(R.string.location_permission)
                                .setMessage(R.string.location_permission_rationale_message)
                                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        token.continuePermissionRequest();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        token.cancelPermissionRequest();
                                    }
                                })
                                .show();
                    }
                }).check();
    }

    @SuppressLint("MissingPermission") // Always will enter here with permission granted
    private void enableMyPositionInMaps() {
        if (map != null) {
            map.setMyLocationEnabled(true);
        }
    }

    private void configureMap() {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = new SupportMapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_map_support, mapFragment).commit();
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        /*
        Styling Google Map:
        https://mapstyle.withgoogle.com/
        https://developers.google.com/maps/documentation/android-api/style-reference
        boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_map));
         */

        map.setBuildingsEnabled(false);
        map.setIndoorEnabled(false);

//        map.setOnInfoWindowClickListener(this);
        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(positionAlcalaCenter, 12));

        presenter.onMapReady();

        checkLocationPermission();
    }


    // INTERACTIONS

    @Override
    public boolean onMarkerClick(Marker marker) {
        Venue venue = (Venue) marker.getTag();
        presenter.onVenuesMarkerClick(venue);
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        presenter.onMapClick();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_venue_more_info:
                presenter.onVenueMoreInfoClick();
                break;

            case R.id.tv_venue_indications:
                presenter.onVenueIndicationsClick();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (viewVenueInfo.getVisibility() == View.VISIBLE) {
            viewVenueInfo.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    // Presenter Callbacks
    @Override
    public void showVenues(final List<Venue> venues) {

        if (map == null) {
            return;
        }

        map.clear();

        LatLngBounds.Builder latlngBuilder = LatLngBounds.builder();
        for (Venue venue : venues) {

            LatLng latLng = new LatLng(venue.getLatitude(), venue.getLongitude());
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker))
            );
            marker.setTag(venue);
            latlngBuilder.include(latLng);


        }

        showMapBounds(latlngBuilder.build());


    }

    public void showMapBounds(LatLngBounds latLngBounds) {
        if (map == null) {
            return;
        }

        int padding = getResources().getDimensionPixelSize(R.dimen.padding_map);
        try {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,
                    padding), 1500, null);
        } catch (Exception e) {
            // Map was not loaded
            map.setOnMapLoadedCallback(() -> {
                if (map != null) {
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, padding), 1500, null);
                }
            });
//            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,
//                    getResources().getDimensionPixelSize(R.dimen.padding_map), 400, 800), 1500, null);
        }
    }

    @Override
    public void showVenueInfo(Venue venue) {

        viewVenueInfo.setVisibility(View.VISIBLE);
        tvVenueName.setText(venue.getName());
        tvVenueDescription.setText(venue.getDescription());

        Picasso.with(this)
                .load(venue.getImageUrlFull())
                .resizeDimen(R.dimen.width_image_big, R.dimen.height_image_big)
                .into(imgVenue);
    }

    @Override
    public void hideVenueInfo() {

        viewVenueInfo.setVisibility(View.GONE);
    }


}
