package com.triskelapps.alcalasuena.ui.venue_info;

import android.os.Bundle;
import android.widget.TextView;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.Venue;

/**
 * Created by julio on 28/05/17.
 */


public class VenueInfoActivity extends BaseActivity implements VenueInfoView {

    VenueInfoPresenter presenter;
    private TextView tvVenueName;

    private void findViews() {
        tvVenueName = (TextView)findViewById( R.id.tv_venue_name );
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = VenueInfoPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_info);
        findViews();

        configureSecondLevelActivity();

        presenter.onCreate(getIntent());

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();

    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }


    //PRESENTER CALLBACKS
    @Override
    public void showVenueInfo(Venue venue) {
        tvVenueName.setText(venue.getName());
    }
}

