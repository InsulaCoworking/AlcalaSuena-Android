package com.triskelapps.alcalasuena.ui;

import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Event;

import java.util.List;

/**
 * Created by julio on 23/05/17.
 */

public interface MainView extends BaseView {


    void showEvents(List<Event> events, String emptyMessage);

    void goToTop();

    void setTabPosition(int position);

    void showNewVersionAvailable();

    void goToEventsTakingPlaceNow(int positionFirstEvent);
}
