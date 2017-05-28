package com.triskelapps.alcalasuena.ui.map;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.Venue;
import com.triskelapps.alcalasuena.util.PermissionHelper;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends BaseActivity implements com.triskelapps.alcalasuena.ui.map.MapView, PermissionHelper.PermissionCallback, MapEventsReceiver, View.OnClickListener {

    private MapView mapView;
    private MapPresenter presenter;

    private final double latNorthAlcala = 40.483310;
    private final double latSouthAlcala = 40.478863;
    private final double lngEastAlcala = -3.360175;
    private final double lngWestAlcala = -3.374004;

    private final GeoPoint pointCenterAlcala = new GeoPoint(40.48261741167103,-3.3674168586730957);

    private MapEventsOverlay mapEventsOverlay;
    private ItemizedOverlay<OverlayItem> mOverlay;
    private MyLocationNewOverlay myLocationOverlay;
    private GpsPlayLocationProvider gpsPlayLocationProvider;
    private RelativeLayout viewVenueInfo;
    private ImageView imgVenue;
    private TextView tvVenueName;
    private TextView tvVenueMoreInfo;
    private TextView tvVenueIndications;
    private TextView tvVenueDescription;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-28 19:01:18 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        viewVenueInfo = (RelativeLayout)findViewById( R.id.view_venue_info );
        imgVenue = (ImageView)findViewById( R.id.img_venue );
        tvVenueName = (TextView)findViewById( R.id.tv_venue_name );
        tvVenueDescription = (TextView)findViewById( R.id.tv_venue_description );
        tvVenueMoreInfo = (TextView)findViewById( R.id.tv_venue_more_info );
        tvVenueIndications = (TextView)findViewById( R.id.tv_venue_indications );
        mapView = (MapView) findViewById(R.id.mapview);

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

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_map);

        configureSecondLevelActivity();

        findViews();

        configureMap();

        presenter.onCreate();

    }

    @Override
    protected void onStart() {
        super.onStart();
        gpsPlayLocationProvider.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gpsPlayLocationProvider.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }



    private void configureMap() {

        mapEventsOverlay = new MapEventsOverlay(this);
        mapView.getOverlays().add(0, mapEventsOverlay);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        mapView.getController().setCenter(pointCenterAlcala);
        mapView.getController().setZoom(16);

    }



    // INTERACTIONS

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuItem_myLocation:
                presenter.onMyLocationButtonClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_venue_more_info:
                presenter.onVenueMoreInfoClick();
                break;

            case R.id.tv_venue_indications:
                presenter.onVenueIndicationsClick(gpsPlayLocationProvider.getLastKnownLocation());
                break;
        }
    }

    // Presenter Callbacks
    @Override
    public void showVenues(final List<Venue> venues) {
        if (mapView.getOverlays().contains(mOverlay)) {
            mapView.getOverlays().remove(mOverlay);
        }

        ArrayList<OverlayItem> items = new ArrayList<>();
        for (Venue venue : venues) {
            OverlayItem overlayItem = new OverlayItem(
                    venue.getId()+"",
                    venue.getName(),
                    venue.getDescription(),
                    new GeoPoint(venue.getLatitude(), venue.getLongitude()));

            overlayItem.setMarker(getResources().getDrawable(R.mipmap.ic_marker));

            items.add(overlayItem); // Lat/Lon decimal degrees
        }

        mOverlay = new ItemizedIconOverlay<>(this, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        mapView.getController().animateTo(item.getPoint());
                        presenter.onVenuesMarkerClick(venues.get(index));
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                });
//        mOverlay.setFocusItemsOnTap(true);

        mapView.getOverlays().add(mOverlay);

//        zoomToBoundingBox(computeArea(items));

    }

    @Override
    public void showVenueInfo(Venue venue) {

        viewVenueInfo.setVisibility(View.VISIBLE);
        tvVenueName.setText(venue.getName());
        tvVenueDescription.setText(venue.getDescription());

        Picasso.with(this)
                .load(venue.getImageUrlFull())
                .into(imgVenue);
    }

    @Override
    public void hideVenueInfo() {

        viewVenueInfo.setVisibility(View.GONE);
    }

    @Override
    public void configureMyLocationOverlay() {

        gpsPlayLocationProvider = new GpsPlayLocationProvider(this);
        this.myLocationOverlay = new MyLocationNewOverlay(gpsPlayLocationProvider, mapView);
        this.myLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(this.myLocationOverlay);
    }

    @Override
    public void goToMyLocation() {

        if (myLocationOverlay != null) {
            GeoPoint myLocation = myLocationOverlay.getMyLocation();
            if (myLocation != null) {
                mapView.getController().animateTo(myLocation);
            } else {
                toast(R.string.location_not_found);
            }
        }
    }

    private void zoomToBoundingBox(final BoundingBox boundingBox) {

        if (mapView.getScreenRect(null).height() > 0) {
            mapView.zoomToBoundingBox(boundingBox, true);
        } else {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    mapView.zoomToBoundingBox(boundingBox, true);
                }
            });
        }
    }

    public BoundingBox computeArea(ArrayList<OverlayItem> items) {

//        double nord = 0, sud = 0, ovest = 0, est = 0;
//
//        for (int i = 0; i < items.size(); i++) {
//            IGeoPoint point = items.get(i).getPoint();
//            if (point == null) continue;
//
//            double lat = point.getLatitude();
//            double lon = point.getLongitude();
//
//            if ((i == 0) || (lat > nord)) nord = lat;
//            if ((i == 0) || (lat < sud)) sud = lat;
//            if ((i == 0) || (lon < ovest)) ovest = lon;
//            if ((i == 0) || (lon > est)) est = lon;
//
//        }

        return new BoundingBox(latNorthAlcala, lngEastAlcala, latSouthAlcala, lngWestAlcala);

    }

    // Permission logic

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(String permission) {
//        switch (permission) {
//            case Manifest.permission.ACCESS_FINE_LOCATION:
////                checkStoragePermissionGranted();
////                break;
////
////            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
//                break;
//        }

        configureMap();
    }

    @Override
    public void onPermissionDenied(String permission) {
//        finish();
        toast("permission denied: " + permission);
    }


    // Map
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        presenter.onMapClick();
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

}
