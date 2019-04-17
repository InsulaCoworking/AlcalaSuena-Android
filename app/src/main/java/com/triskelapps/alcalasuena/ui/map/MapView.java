package com.triskelapps.alcalasuena.ui.map;

import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Venue;

import java.util.List;

/**
 * Created by julio on 28/05/17.
 */

public interface MapView extends BaseView {

    void showVenues(List<Venue> venues);

    void showVenueInfo(Venue venue);

    void hideVenueInfo();

}
