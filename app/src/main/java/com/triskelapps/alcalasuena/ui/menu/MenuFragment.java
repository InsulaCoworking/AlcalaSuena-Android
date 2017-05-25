package com.triskelapps.alcalasuena.ui.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseFragment;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.ui.bands.BandsPresenter;

/**
 * Created by julio on 23/05/17.
 */

public class MenuFragment extends BaseFragment implements View.OnClickListener {
    private TextView btnMenuBands;
    private TextView btnMenuMap;
    private TextView btnMenuShare;

    private void findViews(View layout) {
        btnMenuBands = (TextView)layout.findViewById( R.id.btn_menu_bands );
        btnMenuMap = (TextView)layout.findViewById( R.id.btn_menu_map );
        btnMenuShare = (TextView)layout.findViewById( R.id.btn_menu_share );

        btnMenuBands.setOnClickListener(this);
        btnMenuMap.setOnClickListener(this);
        btnMenuShare.setOnClickListener(this);
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_menu, container, false);
        findViews(layout);

        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu_bands:
                startActivity(BandsPresenter.newBandsActivity(getActivity()));
                break;
        }
    }
}
