package com.triskelapps.alcalasuena.ui.venue_info;

import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.Venue;

import java.util.List;

/**
 * Created by julio on 28/05/17.
 */

public interface VenueInfoView extends BaseView {

    void showVenueInfo(Venue venue, List<Event> eventsVenue);
}
