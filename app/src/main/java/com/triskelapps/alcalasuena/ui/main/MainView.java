package com.triskelapps.alcalasuena.ui.main;

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

    void goToEventsTakingPlaceNow(int positionFirstEvent);

    void showSendNewsButton();

    void showProgressHappeningNow(boolean show);

    void showEventDataNotPreparedView(boolean show);

    void checkNotificationsPermission();
}