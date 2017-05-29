package com.triskelapps.alcalasuena.ui.band_info;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;

import java.util.List;

/**
 * Created by julio on 25/05/17.
 */

public class BandInfoActivity extends BaseActivity implements BandInfoView, View.OnClickListener, EventsBandAdapter.OnItemClickListener {


    private BandInfoPresenter presenter;
    private TextView tvBandName;
    private ImageView imgBand;
    private TextView tvBandGenre;
    private TextView tvBandDescription;
    private ImageView imgFacebook;
    private ImageView imgTwitter;
    private ImageView imgYoutube;
    private ImageView imgBandcamp;
    private ImageView imgPresskit;
    private RecyclerView recyclerEventsBand;
    private EventsBandAdapter adapter;


    private void findViews() {
        tvBandName = (TextView) findViewById(R.id.tv_band_name);
        imgBand = (ImageView) findViewById(R.id.img_band);
        tvBandGenre = (TextView) findViewById(R.id.tv_band_genre);
        tvBandDescription = (TextView) findViewById(R.id.tv_band_description);
        imgFacebook = (ImageView) findViewById(R.id.img_facebook);
        imgTwitter = (ImageView) findViewById(R.id.img_twitter);
        imgYoutube = (ImageView) findViewById(R.id.img_youtube);
        imgBandcamp = (ImageView) findViewById(R.id.img_bandcamp);
        imgPresskit= (ImageView) findViewById(R.id.img_presskit);
        recyclerEventsBand = (RecyclerView) findViewById(R.id.recycler_events_band);

        imgFacebook.setOnClickListener(this);
        imgTwitter.setOnClickListener(this);
        imgYoutube.setOnClickListener(this);
        imgBandcamp.setOnClickListener(this);
        imgPresskit.setOnClickListener(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = BandInfoPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_info);

        findViews();

        configureSecondLevelActivity();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerEventsBand.setLayoutManager(layoutManager);

        presenter.onCreate(getIntent());
    }



    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showBand(Band band, List<Event> eventsBand) {

        tvBandName.setText(band.getName());
        tvBandGenre.setText(band.getGenreOrTag());
        tvBandDescription.setText(band.getDescription());

        if (band.hasValidImage()) {
            imgBand.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(band.getImageCoverUrlFull())
                    .placeholder(R.mipmap.img_default_grid)
                    .error(R.mipmap.img_default_grid)
                    .into(imgBand);
        } else {
            imgBand.setVisibility(View.GONE);
        }

        imgFacebook.setVisibility(band.getFacebook_link() != null ? View.VISIBLE : View.GONE);
        imgTwitter.setVisibility(band.getTwitter_link() != null ? View.VISIBLE : View.GONE);
        imgYoutube.setVisibility(band.getYoutube_link() != null ? View.VISIBLE : View.GONE);
        imgBandcamp.setVisibility(band.getBandcamp_link() != null ? View.VISIBLE : View.GONE);
        imgPresskit.setVisibility(band.getPresskit_link() != null ? View.VISIBLE : View.GONE);

        if (adapter == null) {

            adapter = new EventsBandAdapter(this, eventsBand);
            adapter.setOnItemClickListener(this);
            recyclerEventsBand.setAdapter(adapter);
        } else {
            adapter.updateData(eventsBand);
        }

    }

    @Override
    public void onClick(View v) {
        presenter.onSocialNetworkClicked(v.getId());
    }

    @Override
    public void onEventFavouriteClicked(int idEvent) {
        presenter.onEventFavouriteClicked(idEvent);
    }
}
