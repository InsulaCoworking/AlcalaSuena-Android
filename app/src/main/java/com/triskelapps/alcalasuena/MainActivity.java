package com.triskelapps.alcalasuena;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;

import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.BandInteractor;
import com.triskelapps.alcalasuena.model.Band;

import java.util.List;

public class MainActivity extends BaseActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };
    private RecyclerView recyclerBands;
    private BandsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        recyclerBands = (RecyclerView) findViewById(R.id.recycler_bands);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerBands.setLayoutManager(layoutManager);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
//                layoutManager.getOrientation());
//        recyclerBands.addItemDecoration(dividerItemDecoration);

//        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(20);
//        recyclerBands.addItemDecoration(spaceItemDecoration);


        new BandInteractor(this, this).getBands(new BandInteractor.BandsCallback() {
            @Override
            public void onResponse(List<Band> bands) {
                showBands(bands);
            }

            @Override
            public void onError(String error) {
                toast(error);
            }
        });
    }

    private void showBands(List<Band> bands) {

        adapter = new BandsAdapter(this, bands);
        recyclerBands.setAdapter(adapter);

    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

}
