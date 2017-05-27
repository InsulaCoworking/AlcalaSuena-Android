package com.triskelapps.alcalasuena.ui.band_info;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.BandInteractor;
import com.triskelapps.alcalasuena.model.Band;

/**
 * Created by julio on 25/05/17.
 */


public class BandInfoPresenter extends BasePresenter {

    private static final String EXTRA_BAND_ID = "extra_band_id";

    private final BandInfoView view;
    private final BandInteractor bandInteractor;
    private int idBand;

    public static Intent newBandInfoActivity(Context context, int idBand) {

        Intent intent = new Intent(context, BandInfoActivity.class);
        intent.putExtra(EXTRA_BAND_ID, idBand);
        return intent;
    }

    public static BandInfoPresenter newInstance(BandInfoView view, Context context) {

        return new BandInfoPresenter(view, context);

    }

    private BandInfoPresenter(BandInfoView view, Context context) {
        super(context, view);

        this.view = view;
        bandInteractor = new BandInteractor(context, view);
    }

    public void onCreate(Intent intent) {

        idBand = intent.getIntExtra(EXTRA_BAND_ID, -1);
        if (idBand == -1) {
            throw new IllegalArgumentException("Band info must pass a idBand argument");
        }

        refreshData();
    }

    public void onResume() {

    }

    public void refreshData() {

        Band band = bandInteractor.getBandDB(idBand);
        view.showBand(band);
    }

    public void onSocialNetworkClicked(int id) {

        String url = null;
        Band band = bandInteractor.getBandDB(idBand);

        switch (id) {
            case R.id.img_facebook:
                url = band.getFacebook_link();
                break;
            case R.id.img_twitter:
                url = band.getTwitter_link();
                break;
            case R.id.img_youtube:
                url = band.getYoutube_link();
                break;
            case R.id.img_bandcamp:
                url = band.getBandcamp_link();
                break;
            case R.id.img_presskit:
                url = band.getPresskit_link();
                break;
        }

        if (url != null && Patterns.WEB_URL.matcher(url).matches()) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }
}
