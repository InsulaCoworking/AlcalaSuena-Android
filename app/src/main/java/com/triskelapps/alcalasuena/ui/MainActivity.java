package com.triskelapps.alcalasuena.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
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
import com.triskelapps.alcalasuena.databinding.ActivityMainBinding;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.ui.bands.BandsPresenter;
import com.triskelapps.alcalasuena.ui.events.EventsAdapter;
import com.triskelapps.alcalasuena.ui.news.send.SendNewsActivity;
import com.triskelapps.alcalasuena.views.animation_adapter.AlphaInAnimationAdapter;
import com.triskelapps.alcalasuena.views.animation_adapter.AnimationAdapter;

import java.util.List;

public class MainActivity extends BaseActivity implements MainView, TabLayout.OnTabSelectedListener, EventsAdapter.OnItemClickListener {

    private MainPresenter presenter;
    private ActivityMainBinding binding;
    private MenuItem itemFav;
    private EventsAdapter adapter;
    private AlphaInAnimationAdapter animationAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = MainPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.contentMain.recyclerEvents.setLayoutManager(layoutManager);

        configureToolbar();
        configureDrawerLayout();
        configureToolbarBackArrowBehaviour();

        configureClickListeners();

        TabLayout tabsDays = binding.contentMain.tabsDays;
        tabsDays.addTab(tabsDays.newTab().setCustomView(getTabView(0)));
        tabsDays.addTab(tabsDays.newTab().setCustomView(getTabView(1)));
        tabsDays.addTab(tabsDays.newTab().setCustomView(getTabView(2)));

        tabsDays.addOnTabSelectedListener(this);

        presenter.onCreate(getIntent());

        if (DebugHelper.SHORTCUT_ACTIVITY != null) {
            startActivity(new Intent(this, DebugHelper.SHORTCUT_ACTIVITY));
        }

    }

    private void configureClickListeners() {

        binding.contentMain.btnShareFavs.setOnClickListener(v -> presenter.onShareFavsButtonClicked());
        binding.btnSendNews.setOnClickListener(v -> startActivity(new Intent(this, SendNewsActivity.class)));
        binding.btnHappeningNow.setOnClickListener(v -> checkLocationPermission());

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.drawerLayout.closeDrawer(Gravity.LEFT);
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

                if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    binding.drawerLayout.closeDrawer(Gravity.LEFT);
                } else {

                    if (binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        binding.drawerLayout.closeDrawer(Gravity.RIGHT);
                    } else {
                        binding.drawerLayout.openDrawer(Gravity.LEFT);
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
            binding.contentMain.viewBtnShareFavs.setVisibility(itemFav.isChecked() && adapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void configureDrawerLayout() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.items_main));
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
    public void onEventClicked(Event event) {
        presenter.onEventClick(event);
    }

    @Override
    public void onEventFavouriteClicked(int idEvent) {
        presenter.onEventFavouriteClicked(idEvent);
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
        if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            binding.drawerLayout.closeDrawer(Gravity.LEFT);
        } else if (binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            binding.drawerLayout.closeDrawer(Gravity.RIGHT);
        }  else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuItem_filter:

                if (binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    binding.drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {

                    if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        binding.drawerLayout.closeDrawer(Gravity.LEFT);
                    }

                    binding.drawerLayout.openDrawer(Gravity.RIGHT);
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
            binding.contentMain.recyclerEvents.setAdapter(animationAdapter);

        } else {

            try {
                // Little trick to avoid animation when searching
                animationAdapter.setDuration(0);
                binding.contentMain.recyclerEvents.getRecycledViewPool().clear();
                adapter.notifyDataSetChanged();
                binding.contentMain.recyclerEvents.post(() -> animationAdapter.setDuration(AnimationAdapter.DEFAULT_DURATION));
            } catch (Exception e) {
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }

        binding.contentMain.tvEmptyMessage.setVisibility(emptyMessage != null ? View.VISIBLE : View.GONE);
        binding.contentMain.tvEmptyMessage.setText(emptyMessage);

        refreshShareFavsVisibility();

    }

    @Override
    public void goToTop() {
        if (adapter.getItemCount() > 0) {
            binding.contentMain.recyclerEvents.scrollToPosition(0);
        }
    }

    @Override
    public void setTabPosition(int position) {
        binding.contentMain.tabsDays.getTabAt(position).select();
    }

    @Override
    public void goToEventsTakingPlaceNow(int positionFirstEvent) {
        binding.contentMain.recyclerEvents.scrollToPosition(positionFirstEvent);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showSendNewsButton() {
        binding.btnSendNews.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressHappeningNow(boolean show) {
        binding.progressHappeningNow.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showEventDataNotPreparedView(boolean show) {

        binding.contentMain.tvEmptyMessage.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.contentMain.btnSeeBands.setVisibility(show ? View.VISIBLE : View.GONE);

        binding.contentMain.tvEmptyMessage.setText(R.string.data_not_prepared);
        binding.contentMain.btnSeeBands.setOnClickListener(v -> startActivity(BandsPresenter.newBandsActivity(this)));

    }
}
