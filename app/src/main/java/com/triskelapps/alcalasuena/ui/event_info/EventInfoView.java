package com.triskelapps.alcalasuena.ui.event_info;

import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Event;

public interface EventInfoView extends BaseView {
    void showEventInfo(Event event);
}
