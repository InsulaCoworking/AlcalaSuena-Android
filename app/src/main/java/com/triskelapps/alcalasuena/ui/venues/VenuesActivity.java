package com.triskelapps.alcalasuena.ui.venues;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.Venue;
import com.triskelapps.alcalasuena.views.SpaceItemDecoration;
import com.triskelapps.alcalasuena.views.TypeWriterTextView;
import com.triskelapps.alcalasuena.views.animation_adapter.AnimationAdapter;
import com.triskelapps.alcalasuena.views.animation_adapter.ScaleInAnimationAdapter;

import java.util.List;

public class VenuesActivity extends BaseActivity implements VenuesView, VenuesAdapter.OnItemClickListener {

    private RecyclerView recyclerVenues;
    private VenuesAdapter adapter;
    private VenuesPresenter presenter;
    private ScaleInAnimationAdapter animationAdapter;
    private TypeWriterTextView typeWriterIntro;

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }


    private void findViews() {
        typeWriterIntro = (TypeWriterTextView) findViewById(R.id.typewriter_venues_intro);
        recyclerVenues = (RecyclerView) findViewById(R.id.recycler_venues);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = VenuesPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venues);

        findViews();

        configureSecondLevelActivity();
        setToolbarTitle(R.string.venues);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
//        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerVenues.setLayoutManager(layoutManager);


//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
//                layoutManager.getOrientation());
//        recyclerVenues.addItemDecoration(dividerItemDecoration);

        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.separation_card_grid));
        recyclerVenues.addItemDecoration(spaceItemDecoration);

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
            recyclerVenues.setAdapter(animationAdapter);

        } else {
            // Little trick to avoid animation when searching
            animationAdapter.setDuration(0);
            adapter.updateData(venues);
            recyclerVenues.post(new Runnable() {
                @Override
                public void run() {
                    animationAdapter.setDuration(AnimationAdapter.DEFAULT_DURATION);
                }
            });
        }

    }

    @Override
    public void animateIntro() {
        typeWriterIntro.animateText(getString(R.string.venues_intro));

    }


}
