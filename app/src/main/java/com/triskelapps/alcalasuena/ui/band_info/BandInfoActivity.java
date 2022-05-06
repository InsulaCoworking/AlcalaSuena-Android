package com.triskelapps.alcalasuena.ui.band_info;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.model.SocialItem;
import com.triskelapps.alcalasuena.views.CircleTransform;

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
    private RecyclerView recyclerEventsBand;
    private EventsBandAdapter adapter;
    private ImageView imgBandRound;
    private RecyclerView recyclerSocialItems;
    private SocialItemsBandAdapter adapterSocialItems;


    private void findViews() {
        tvBandName = (TextView) findViewById(R.id.tv_band_name);
        imgBand = (ImageView) findViewById(R.id.img_band);
        imgBandRound = (ImageView) findViewById(R.id.img_band_round);
        tvBandGenre = (TextView) findViewById(R.id.tv_band_genre);
        tvBandDescription = (TextView) findViewById(R.id.tv_band_description);
        recyclerEventsBand = (RecyclerView) findViewById(R.id.recycler_events_band);
        recyclerSocialItems = (RecyclerView) findViewById(R.id.recycler_social_items);

        imgBand.setOnClickListener(this);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_band, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // INTERACTIONS

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuItem_share_band:
                presenter.onShareBandClick();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //PRESENTER CALLBACKS

    @Override
    public void showBand(Band band, List<Event> eventsBand) {

        tvBandName.setText(band.getName());
        tvBandGenre.setText(band.getGenreOrTag());
//        Util.setHtmlLinkableText(tvBandDescription, band.getDescription());
        tvBandDescription.setText(band.getDescription());

        if (band.hasValidImage()) {
            imgBand.setVisibility(View.VISIBLE);
            imgBandRound.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(band.getImageCoverUrlFull())
                    .placeholder(R.mipmap.img_default_grid)
                    .error(R.mipmap.img_default_grid)
                    .resizeDimen(R.dimen.width_image_big, R.dimen.height_image_big)
                    .into(imgBand);

            Picasso.get()
                    .load(band.getImageLogoUrlFull())
                    .placeholder(R.mipmap.img_default_grid)
                    .error(R.mipmap.img_default_grid)
                    .transform(new CircleTransform())
                    .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                    .into(imgBandRound);
        } else {
            imgBand.setVisibility(View.GONE);
            imgBandRound.setVisibility(View.GONE);
        }

        if (adapter == null) {

            adapter = new EventsBandAdapter(this, eventsBand);
            adapter.setOnItemClickListener(this);
            recyclerEventsBand.setAdapter(adapter);
        } else {
            adapter.updateData(eventsBand);
            recyclerEventsBand.getRecycledViewPool().clear();
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void showSocialItems(List<SocialItem> socialItems) {

        if (adapterSocialItems == null) {
            adapterSocialItems = new SocialItemsBandAdapter(this, socialItems);
            recyclerSocialItems.setAdapter(adapterSocialItems);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_band:
                presenter.onImageBandClick();
                break;

        }
    }

    @Override
    public void onEventFavouriteClicked(int idEvent) {
        presenter.onEventFavouriteClicked(idEvent);
    }
}
