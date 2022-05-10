package com.triskelapps.alcalasuena.ui.filter;

import android.content.Context;

import com.triskelapps.alcalasuena.App;
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

        List<Tag> tags = App.getDB().tagDao().getAll();
        view.showTags(tags);

    }

    public void onTagClick(String idTag) {

        boolean allTagsActive = App.getDB().tagStateDao().getInactive().isEmpty();
        if (allTagsActive) {
            App.getDB().tagStateDao().inactiveAll();
            App.getDB().tagStateDao().setActive(idTag);
        } else {
            App.getDB().tagStateDao().toggleState(idTag);
        }

        boolean allTagsInactive = App.getDB().tagStateDao().getActive().isEmpty();
        if (allTagsInactive) {
            App.getDB().tagStateDao().activeAll();
        }

        refreshData();

        ((MainPresenter)((MainActivity)context).getPresenter()).refreshData();

    }

    public void onAllTagsButtonClick() {
        App.getDB().tagStateDao().activeAll();
        refreshData();

        ((MainPresenter)((MainActivity)context).getPresenter()).refreshData();
    }
}
