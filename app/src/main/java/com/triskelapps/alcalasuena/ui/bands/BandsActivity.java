package com.triskelapps.alcalasuena.ui.bands;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.Band;

import java.util.List;

public class BandsActivity extends BaseActivity implements BandsView, TextWatcher, BandsAdapter.OnItemClickListener {

    private RecyclerView recyclerBands;
    private BandsAdapter adapter;
    private BandsPresenter presenter;
    private EditText editSearch;
    private View viewNoResults;

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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerBands.setLayoutManager(layoutManager);


//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
//                layoutManager.getOrientation());
//        recyclerBands.addItemDecoration(dividerItemDecoration);

//        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(20);
//        recyclerBands.addItemDecoration(spaceItemDecoration);


        presenter.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void showBands(List<Band> bands) {

        if (adapter == null) {

            adapter = new BandsAdapter(this, bands);
            recyclerBands.setAdapter(adapter);

            adapter.setOnItemClickListener(this);
        } else {
            adapter.updateData(bands);
        }

        viewNoResults.setVisibility(bands != null && !bands.isEmpty() ? View.GONE : View.VISIBLE);

    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
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

    @Override
    public void onItemClick(View view, int position) {

    }
}
