package com.triskelapps.alcalasuena.ui.bands;

import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Band;

import java.util.List;

/**
 * Created by julio on 23/05/17.
 */

public interface BandsView extends BaseView {


    void showBands(List<Band> bands);
}
