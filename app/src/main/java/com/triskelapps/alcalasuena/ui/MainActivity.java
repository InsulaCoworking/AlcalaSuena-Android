package com.triskelapps.alcalasuena.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.triskelapps.alcalasuena.DebugHelper;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.ui.events.EventsAdapter;
import com.triskelapps.alcalasuena.ui.news.send.SendNewsActivity;
import com.triskelapps.alcalasuena.views.animation_adapter.AlphaInAnimationAdapter;
import com.triskelapps.alcalasuena.views.animation_adapter.AnimationAdapter;

import java.util.List;

public class MainActivity extends BaseActivity implements MainView, TabLayout.OnTabSelectedListener, EventsAdapter.OnItemClickListener, View.OnClickListener {

    private RecyclerView recyclerEvents;
    private EventsAdapter adapter;
    private TabLayout tabsDays;
    private MainPresenter presenter;
    private DrawerLayout drawerLayout;
    private MenuItem itemFav;
    private TextView tvEmptyMessage;
    private TextView btnShareFavs;
    private AlphaInAnimationAdapter animationAdapter;
    private View viewNewVersion;
    private View btnHideNewVersionView;
    private View btnUpdateNewVersion;
    private View viewBtnShareFavs;
    private View btnSendNews;
    private View btnHappeningNow;
    private View progressHappeningNow;

    private void findViews() {
        tabsDays = (TabLayout) findViewById(R.id.tabs_days);
        recyclerEvents = (RecyclerView) findViewById(R.id.recycler_events);
        tvEmptyMessage = (TextView) findViewById(R.id.tv_empty_message);
        btnShareFavs = (TextView) findViewById(R.id.btn_share_favs);
        viewBtnShareFavs = findViewById(R.id.view_btn_share_favs);

        viewNewVersion = findViewById(R.id.view_new_version);
        btnHideNewVersionView = findViewById(R.id.btn_hide_new_version);
        btnUpdateNewVersion = findViewById(R.id.btn_update_new_version);

        btnSendNews = findViewById(R.id.btn_send_news);
        btnHappeningNow = findViewById(R.id.btn_happening_now);
        progressHappeningNow = findViewById(R.id.progress_happening_now);

        btnShareFavs.setOnClickListener(this);
        btnHideNewVersionView.setOnClickListener(this);
        btnUpdateNewVersion.setOnClickListener(this);

        btnSendNews.setOnClickListener(this);
        btnHappeningNow.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = MainPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerEvents.setLayoutManager(layoutManager);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
//                layoutManager.getOrientation());
//        recyclerBands.addItemDecoration(dividerItemDecoration);

//        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(20);
//        recyclerBands.addItemDecoration(spaceItemDecoration);

        configureToolbar();
        configureDrawerLayout();
        configureToolbarBackArrowBehaviour();

        setImageTitle(R.mipmap.img_title_alcalasuena);

        tabsDays.addTab(tabsDays.newTab().setCustomView(getTabView(0)));
        tabsDays.addTab(tabsDays.newTab().setCustomView(getTabView(1)));
        tabsDays.addTab(tabsDays.newTab().setCustomView(getTabView(2)));

        tabsDays.addOnTabSelectedListener(this);

        presenter.onCreate(getIntent());

        if (DebugHelper.SHORTCUT_ACTIVITY != null) {
            startActivity(new Intent(this, DebugHelper.SHORTCUT_ACTIVITY));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        drawerLayout.closeDrawer(Gravity.LEFT);
        presenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }


    private void configureToolbarBackArrowBehaviour() {

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {

                    if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                    } else {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    }

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        itemFav = menu.findItem(R.id.menuItem_fav);
        return super.onCreateOptionsMenu(menu);
    }

    private void refreshShareFavsVisibility() {
        if (itemFav != null) {
            viewBtnShareFavs.setVisibility(itemFav.isChecked() && adapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void configureDrawerLayout() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.items_main));
    }

    private View getTabView(int day) {
        View tabView = View.inflate(this, R.layout.view_tab, null);
        TextView tvDayWeek = (TextView) tabView.findViewById(R.id.tv_tab_day_week);
        TextView tvDayMonth = (TextView) tabView.findViewById(R.id.tv_tab_day_month);

        tvDayWeek.setText(presenter.getWeekDayForTabPosition(day));
        tvDayMonth.setText(presenter.getDayMonthForTabPosition(day));

        return tabView;
    }

    // INTERACTIONS
    @Override
    public void onBandClicked(int idBand) {
        presenter.onBandClicked(idBand);
    }

    @Override
    public void onEventFavouriteClicked(int idEvent) {
        presenter.onEventFavouriteClicked(idEvent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_share_favs:
                presenter.onShareFavsButtonClicked();
                break;

            case R.id.btn_update_new_version:
                presenter.onUpdateVersionClick();
                break;

            case R.id.btn_hide_new_version:
                viewNewVersion.setVisibility(View.GONE);
                break;

            case R.id.btn_send_news:
                startActivity(new Intent(this, SendNewsActivity.class));
                break;

            case R.id.btn_happening_now:
                checkLocationPermission();
                break;
        }
    }

    private void checkLocationPermission() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        presenter.onHappeningNowButtonClick();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        toast(R.string.location_wont_show);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, final PermissionToken token) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(R.string.location_permission)
                                .setMessage(R.string.location_permission_rationale_happening_now_message)
                                .setPositiveButton(R.string.accept, (dialog, which) -> token.continuePermissionRequest())
                                .setNegativeButton(R.string.cancel, (dialog, which) -> token.cancelPermissionRequest())
                                .show();
                    }
                }).check();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        presenter.onTabSelected(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }  else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuItem_filter:

                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {

                    if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    }

                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
                return true;


            case R.id.menuItem_fav:
                itemFav.setChecked(!itemFav.isChecked());
                itemFav.setIcon(itemFav.isChecked() ? R.mipmap.ic_star_selected : R.mipmap.ic_star_unselected);
                presenter.onFilterFavouritesClicked(itemFav.isChecked());

                refreshShareFavsVisibility();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // PRESENTER CALLBACKS
    @Override
    public void showEvents(List<Event> events, String emptyMessage) {

        if (adapter == null) {

            adapter = new EventsAdapter(this, events);
            adapter.setOnItemClickListener(this);

            animationAdapter = new AlphaInAnimationAdapter(adapter);
            animationAdapter.setFirstOnly(false);
            recyclerEvents.setAdapter(animationAdapter);

        } else {

            try {
                // Little trick to avoid animation when searching
                animationAdapter.setDuration(0);
//                adapter.updateData(events);
                recyclerEvents.getRecycledViewPool().clear();
                adapter.notifyDataSetChanged();
                recyclerEvents.post(new Runnable() {
                    @Override
                    public void run() {
                        animationAdapter.setDuration(AnimationAdapter.DEFAULT_DURATION);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Crashlytics.logException(e);
            }
        }

        tvEmptyMessage.setVisibility(emptyMessage != null ? View.VISIBLE : View.GONE);
        tvEmptyMessage.setText(emptyMessage);

        refreshShareFavsVisibility();

    }

    @Override
    public void goToTop() {
        if (adapter.getItemCount() > 0) {
            recyclerEvents.scrollToPosition(0);
        }
    }

    @Override
    public void setTabPosition(int position) {
        tabsDays.getTabAt(position).select();
    }

    @Override
    public void showNewVersionAvailable() {

        viewNewVersion.setVisibility(View.VISIBLE);
    }

    @Override
    public void goToEventsTakingPlaceNow(int positionFirstEvent) {
        recyclerEvents.scrollToPosition(positionFirstEvent);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showSendNewsButton() {
        btnSendNews.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressHappeningNow(boolean show) {
        progressHappeningNow.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }
}
