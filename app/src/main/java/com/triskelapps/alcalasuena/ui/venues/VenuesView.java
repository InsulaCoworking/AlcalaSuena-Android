package com.triskelapps.alcalasuena.ui.venues;

import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Venue;

import java.util.List;

/**
 * Created by julio on 23/05/17.
 */

public interface VenuesView extends BaseView {


    void showVenues(List<Venue> venues);
}
