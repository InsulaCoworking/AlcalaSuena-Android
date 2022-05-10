package com.triskelapps.alcalasuena.ui.venues;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.databinding.ActivityVenuesBinding;
import com.triskelapps.alcalasuena.model.Venue;
import com.triskelapps.alcalasuena.views.TypeWriterTextView;
import com.triskelapps.alcalasuena.views.animation_adapter.AnimationAdapter;
import com.triskelapps.alcalasuena.views.animation_adapter.ScaleInAnimationAdapter;

import java.util.List;

public class VenuesActivity extends BaseActivity implements VenuesView, VenuesAdapter.OnItemClickListener {

    private VenuesAdapter adapter;
    private VenuesPresenter presenter;
    private ScaleInAnimationAdapter animationAdapter;
    private ActivityVenuesBinding binding;

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = VenuesPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        binding = ActivityVenuesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureSecondLevelActivity();
        setToolbarTitle(R.string.venues);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
//        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerVenues.setLayoutManager(layoutManager);
        binding.recyclerVenues.setNestedScrollingEnabled(false);


//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
//                layoutManager.getOrientation());
//        recyclerVenues.addItemDecoration(dividerItemDecoration);

//        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.separation_card_grid));
//        recyclerVenues.addItemDecoration(spaceItemDecoration);

        presenter.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    // INTERACTIONS

    @Override
    public void onItemClick(View view, int position, int idVenue) {

        presenter.onVenueClicked(idVenue);

    }



    // PRESENTER CALLBACKS
    @Override
    public void showVenues(List<Venue> venues) {

        if (adapter == null) {

            adapter = new VenuesAdapter(this, venues);
            adapter.setOnItemClickListener(this);

            animationAdapter = new ScaleInAnimationAdapter(adapter);
            animationAdapter.setFirstOnly(false);
            binding.recyclerVenues.setAdapter(animationAdapter);

        } else {
            // Little trick to avoid animation when searching
            animationAdapter.setDuration(0);
            adapter.updateData(venues);
            binding.recyclerVenues.getRecycledViewPool().clear();
            adapter.notifyDataSetChanged();
            binding.recyclerVenues.post(() -> animationAdapter.setDuration(AnimationAdapter.DEFAULT_DURATION));
        }

        binding.tvVenuesEmptyView.setVisibility(venues.isEmpty() ? View.VISIBLE : View.GONE);

    }

    @Override
    public void animateIntro() {
        binding.typewriterVenuesIntro.animateText(getString(R.string.venues_intro));
    }


}
