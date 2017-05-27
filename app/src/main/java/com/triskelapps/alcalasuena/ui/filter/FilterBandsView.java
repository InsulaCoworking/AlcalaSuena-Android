package com.triskelapps.alcalasuena.ui.filter;

import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Tag;

import java.util.List;

/**
 * Created by julio on 27/05/17.
 */

public interface FilterBandsView extends BaseView {


    void showTags(List<Tag> tags);
}
