package com.triskelapps.alcalasuena.ui.bands;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.views.animation_adapter.AnimationAdapter;
import com.triskelapps.alcalasuena.views.animation_adapter.ScaleInAnimationAdapter;

import java.util.List;

public class BandsActivity extends BaseActivity implements BandsView, TextWatcher, BandsAdapter.OnItemClickListener {

    private RecyclerView recyclerBands;
    private BandsAdapter adapter;
    private BandsPresenter presenter;
    private EditText editSearch;
    private View viewNoResults;
    private ScaleInAnimationAdapter animationAdapter;

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }


    private void findViews() {
        editSearch = (EditText) findViewById(R.id.edit_search);
        recyclerBands = (RecyclerView) findViewById(R.id.recycler_bands);
        viewNoResults = findViewById(R.id.view_no_results);

        editSearch.addTextChangedListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = BandsPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bands);

        findViews();

        configureSecondLevelActivity();

        setToolbarTitle(R.string.bands);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
//        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerBands.setLayoutManager(layoutManager);


//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
//                layoutManager.getOrientation());
//        recyclerBands.addItemDecoration(dividerItemDecoration);

//        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.separation_card_grid));
//        recyclerBands.addItemDecoration(spaceItemDecoration);


        presenter.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    // INTERACTIONS

    @Override
    public void onItemClick(View view, int position, int idBand) {

        presenter.onBandClicked(idBand);

    }



    // PRESENTER CALLBACKS
    @Override
    public void showBands(List<Band> bands) {

        if (adapter == null) {

            adapter = new BandsAdapter(this, bands);
            adapter.setOnItemClickListener(this);

            animationAdapter = new ScaleInAnimationAdapter(adapter);
            animationAdapter.setFirstOnly(false);
            recyclerBands.setAdapter(animationAdapter);

        } else {
            // Little trick to avoid animation when searching
            animationAdapter.setDuration(0);
            adapter.updateData(bands);
            recyclerBands.getRecycledViewPool().clear();
            adapter.notifyDataSetChanged();
            recyclerBands.post(new Runnable() {
                @Override
                public void run() {
                    animationAdapter.setDuration(AnimationAdapter.DEFAULT_DURATION);
                }
            });
        }

        viewNoResults.setVisibility(bands != null && !bands.isEmpty() ? View.GONE : View.VISIBLE);

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        presenter.onSearchTextChanged(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
