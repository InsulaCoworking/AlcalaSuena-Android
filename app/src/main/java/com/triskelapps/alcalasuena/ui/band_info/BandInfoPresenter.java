package com.triskelapps.alcalasuena.ui.band_info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.BandInteractor;
import com.triskelapps.alcalasuena.interactor.EventInteractor;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.SocialItem;
import com.triskelapps.alcalasuena.ui.image_full.ImageFullActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julio on 25/05/17.
 */


public class BandInfoPresenter extends BasePresenter {

    private static final String EXTRA_BAND_ID = "extra_band_id";

    private final BandInfoView view;
    private final BandInteractor bandInteractor;
    private final EventInteractor eventInteractor;
    private Band band;

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
        eventInteractor = new EventInteractor(context, view);
    }

    public void onCreate(Intent intent) {

        int idBand = intent.getIntExtra(EXTRA_BAND_ID, -1);
        if (idBand == -1) {
            throw new IllegalArgumentException("Band info must pass a idBand argument");
        }

        band = App.getDB().bandDao().getBandById(idBand);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, band.getId()+"");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, band.getName());
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

        refreshData();

    }

    public void onResume() {

    }

    public void refreshData() {

        List<Event> eventsBand = eventInteractor.getEventsForBand(band.getId());
        view.showBand(band, eventsBand);

        configureSocialItems(band);
    }

    private void configureSocialItems(Band band) {
        List<SocialItem> socialItems = new ArrayList<>();
        if (isValidUrl(band.getFacebook_link())) {
            socialItems.add(new SocialItem(R.mipmap.ic_facebook, band.getFacebook_link()));
        }
        if (isValidUrl(band.getTwitter_link())) {
            socialItems.add(new SocialItem(R.mipmap.ic_twitter, band.getTwitter_link()));
        }
        if (isValidUrl(band.getYoutube_link())) {
            socialItems.add(new SocialItem(R.mipmap.ic_youtube, band.getYoutube_link()));
        }
        if (isValidUrl(band.getInstagram_link())) {
            socialItems.add(new SocialItem(R.mipmap.ic_instagram, band.getInstagram_link()));
        }
        if (isValidUrl(band.getWebpage_link())) {
            socialItems.add(new SocialItem(R.mipmap.ic_web, band.getWebpage_link()));
        }
        if (isValidUrl(band.getSpotify_link())) {
            socialItems.add(new SocialItem(R.mipmap.ic_spotify, band.getSpotify_link()));
        }
        if (isValidUrl(band.getBandcamp_link())) {
            socialItems.add(new SocialItem(R.mipmap.ic_bandcamp, band.getBandcamp_link()));
        }
        if (isValidUrl(band.getPresskit_link())) {
            socialItems.add(new SocialItem(R.mipmap.ic_presskit, band.getPresskit_link()));
        }


        view.showSocialItems(socialItems);
    }


    private boolean isValidUrl(String url) {
        return url != null && Patterns.WEB_URL.matcher(url).matches();
    }

    public void onEventFavouriteClicked(int idEvent) {
        eventInteractor.toggleFavState(idEvent);
        refreshData();
    }

    public void onImageBandClick() {

        context.startActivity(ImageFullActivity.newImageFullActivity(context, band.getImageCoverUrlFull().toString()));
    }

    public void onShareBandClick() {
        String urlBandWeb = band.getUrlBandWeb();
        String text = String.format(context.getString(R.string.send_band_text), urlBandWeb);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        context.startActivity(intent);
    }
}
