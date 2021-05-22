package com.triskelapps.alcalasuena.ui.event_info;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.databinding.ActivityEventInfoBinding;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.ui.bands.BandsAdapter;
import com.triskelapps.alcalasuena.views.CircleTransform;

public class EventInfoActivity extends BaseActivity implements EventInfoView {

    private ActivityEventInfoBinding binding;
    private EventInfoPresenter presenter;
    private BandsAdapter adapter;

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventInfoBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        configureSecondLevelActivity();

        binding.btnBuyTickets.setOnClickListener(v -> presenter.onBuyTicketsClick());

        presenter = EventInfoPresenter.newInstance(this, this);
        presenter.onCreate(getIntent());


    }


    @Override
    public void showEventInfo(Event event) {

        Picasso.with(this)
                .load(event.getImageUrlFull())
                .placeholder(R.mipmap.img_default_grid)
                .error(R.mipmap.img_default_grid)
                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                .into(binding.imgEvent);

        if (adapter == null) {
            adapter = new BandsAdapter(this, event.getBands());
            adapter.setOnItemClickListener((view, position, id) -> {
                presenter.onBandClick(id);
            });
            binding.recyclerBandsEvent.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        binding.viewEventInfo.tvEventBandDay.setText(event.getDayShareFormat());
        binding.viewEventInfo.tvEventBandTime.setText(event.getTimeFormatted());
        binding.viewEventInfo.tvEventBandVenue.setText(event.getVenue().getName());
        binding.viewEventInfo.tvEventBandDuration.setText(event.getDurationFormatted(this));

        binding.viewEventInfo.imgStarred.setSelected(event.isStarred());

//        binding.viewEventInfo.imgStarred.setOnClickListener(v -> itemClickListener.onEventFavouriteClicked(getItemAtPosition(holder.getAdapterPosition()).getId()));
    }
}