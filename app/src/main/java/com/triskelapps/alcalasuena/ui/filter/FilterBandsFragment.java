package com.triskelapps.alcalasuena.ui.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseFragment;
import com.triskelapps.alcalasuena.base.BasePresenter;

/**
 * Created by julio on 23/05/17.
 */

public class FilterBandsFragment extends BaseFragment {


    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_filter_bands, container, false);

        return layout;
    }
}
