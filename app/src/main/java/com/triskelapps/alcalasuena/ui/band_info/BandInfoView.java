package com.triskelapps.alcalasuena.ui.band_info;

import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;

import java.util.List;

/**
 * Created by julio on 25/05/17.
 */

public interface BandInfoView extends BaseView {

    void showBand(Band band, List<Event> eventsBand);
}
