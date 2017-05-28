package com.triskelapps.alcalasuena.ui.filter;

import android.content.Context;

import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.BandInteractor;
import com.triskelapps.alcalasuena.model.Filter;
import com.triskelapps.alcalasuena.model.Tag;
import com.triskelapps.alcalasuena.ui.MainActivity;
import com.triskelapps.alcalasuena.ui.MainPresenter;

import java.util.List;

/**
 * Created by julio on 27/05/17.
 */


public class FilterBandsPresenter extends BasePresenter {

    private final FilterBandsView view;
    private final BandInteractor bandInteractor;
    private Filter filter = new Filter();

    public static FilterBandsPresenter newInstance(FilterBandsView view, Context context) {

        return new FilterBandsPresenter(view, context);

    }

    private FilterBandsPresenter(FilterBandsView view, Context context) {
        super(context, view);

        this.view = view;
        bandInteractor = new BandInteractor(context, view);

    }

    public void onCreate() {

        // todo get from db
//        if (bandInteractor.getTags().isEmpty()) {
//            bandInteractor.initializeMockTags();
//        }

        refreshData();

    }

    public void onResume() {

    }

    public void refreshData() {

        List<Tag> tags = bandInteractor.getTags();
        view.showTags(tags);

    }

    public void onTagClick(String idTag) {

        if (bandInteractor.areAllTagsActive()) {
            bandInteractor.setAllTagsInactiveUnlessThisOne(idTag);
        } else {
            bandInteractor.toggleTagState(idTag);
        }

        if (bandInteractor.areAllTagsInactive()) {
            bandInteractor.setAllTagsActive();
        }

        refreshData();

        ((MainPresenter)((MainActivity)context).getPresenter()).refreshData();

    }

    public void onAllTagsButtonClick() {
        bandInteractor.setAllTagsActive();
        refreshData();

        ((MainPresenter)((MainActivity)context).getPresenter()).refreshData();
    }
}
