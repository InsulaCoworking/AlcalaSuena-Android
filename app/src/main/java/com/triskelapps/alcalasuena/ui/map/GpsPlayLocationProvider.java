package com.triskelapps.alcalasuena.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;

/**
 * Created by julio on 28/05/17.
 */

public class GpsPlayLocationProvider implements IMyLocationProvider,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private final Context context;
    private GoogleApiClient mGoogleApiClient;
    private IMyLocationConsumer myLocationConsumer;
    private Location mLastLocation;
    private GpsPlayProviderListener gpsPlayProviderListener;

    public GpsPlayLocationProvider(Context context) {

        this.context = context;

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void onStart() {
    }


    public void onStop() {
    }

    @Override
    public boolean startLocationProvider(IMyLocationConsumer myLocationConsumer) {
        this.myLocationConsumer = myLocationConsumer;

        mGoogleApiClient.connect();

        return true;
    }

    @Override
    public void stopLocationProvider() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public Location getLastKnownLocation() {
        return mLastLocation;
    }

    @Override
    public void destroy() {
        mGoogleApiClient = null;
    }


    // Play
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (gpsPlayProviderListener != null) {
                gpsPlayProviderListener.onLocationPermissionNeeded();
            }

        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            LocationRequest mLocationRequest = new LocationRequest();
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //Play Location Listener
    @Override
    public void onLocationChanged(Location location) {
        myLocationConsumer.onLocationChanged(location, this);
        mLastLocation = location;
    }


    public void setOnGpsPlayProviderListener(GpsPlayProviderListener listener) {
        this.gpsPlayProviderListener = listener;
    }


    public interface GpsPlayProviderListener{
        void onLocationPermissionNeeded();
    }
}
