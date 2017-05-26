package com.triskelapps.alcalasuena.ui.band_info;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.Band;

/**
 * Created by julio on 25/05/17.
 */

public class BandInfoActivity extends BaseActivity implements BandInfoView, View.OnClickListener {


    private BandInfoPresenter presenter;
    private TextView tvBandName;
    private ImageView imgBand;
    private TextView tvBandGenre;
    private TextView tvBandDescription;
    private ImageView imgFacebook;
    private ImageView imgTwitter;
    private ImageView imgYoutube;
    private ImageView imgBandcamp;


    private void findViews() {
        tvBandName = (TextView) findViewById(R.id.tv_band_name);
        imgBand = (ImageView) findViewById(R.id.img_band);
        tvBandGenre = (TextView) findViewById(R.id.tv_band_genre);
        tvBandDescription = (TextView) findViewById(R.id.tv_band_description);
        imgFacebook = (ImageView) findViewById(R.id.img_facebook);
        imgTwitter = (ImageView) findViewById(R.id.img_twitter);
        imgYoutube = (ImageView) findViewById(R.id.img_youtube);
        imgBandcamp = (ImageView) findViewById(R.id.img_bandcamp);

        imgFacebook.setOnClickListener(this);
        imgTwitter.setOnClickListener(this);
        imgYoutube.setOnClickListener(this);
        imgBandcamp.setOnClickListener(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = BandInfoPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_info);

        findViews();

        configureSecondLevelActivity();
        presenter.onCreate(getIntent());
    }


    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showBand(Band band) {

        tvBandName.setText(band.getName());
        tvBandGenre.setText(band.getGenre());
        tvBandDescription.setText(band.getDescription());

        Picasso.with(this)
                .load(band.getImageUrlFull())
                .placeholder(R.mipmap.img_default_grid)
                .error(R.mipmap.img_default_grid)
                .into(imgBand);

        imgFacebook.setVisibility(band.getFacebook_link() != null ? View.VISIBLE : View.GONE);
        imgTwitter.setVisibility(band.getTwitter_link() != null ? View.VISIBLE : View.GONE);
        imgYoutube.setVisibility(band.getYoutube_link() != null ? View.VISIBLE : View.GONE);
        imgBandcamp.setVisibility(band.getBandcamp_link() != null ? View.VISIBLE : View.GONE);

    }

    @Override
    public void onClick(View v) {
        presenter.onSocialNetworkClicked(v.getId());
    }
}
