package com.triskelapps.alcalasuena.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.triskelapps.alcalasuena.DebugHelper;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.ui.events.EventsAdapter;
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

    private void findViews() {
        tabsDays = (TabLayout) findViewById(R.id.tabs_days);
        recyclerEvents = (RecyclerView) findViewById(R.id.recycler_events);
        tvEmptyMessage = (TextView) findViewById(R.id.tv_empty_message);
        btnShareFavs = (TextView) findViewById(R.id.btn_share_favs);

        viewNewVersion = findViewById(R.id.view_new_version);
        btnHideNewVersionView = findViewById(R.id.btn_hide_new_version);
        btnUpdateNewVersion = findViewById(R.id.btn_update_new_version);

        btnShareFavs.setOnClickListener(this);
        btnHideNewVersionView.setOnClickListener(this);
        btnUpdateNewVersion.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = MainPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        if (DebugHelper.SHORTCUT_ACTIVITY != null) {
            startActivity(new Intent(this, DebugHelper.SHORTCUT_ACTIVITY));
        }

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

        tabsDays.addTab(tabsDays.newTab().setCustomView(getTabView(1)));
        tabsDays.addTab(tabsDays.newTab().setCustomView(getTabView(2)));
        tabsDays.addTab(tabsDays.newTab().setCustomView(getTabView(3)));

        tabsDays.addOnTabSelectedListener(this);

        presenter.onCreate(getIntent());
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
        Drawable iconFilter = getResources().getDrawable(R.mipmap.ic_filter);
        iconFilter.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        menu.findItem(R.id.menuItem_filter).setIcon(iconFilter);

        itemFav = menu.findItem(R.id.menuItem_fav);
        return super.onCreateOptionsMenu(menu);
    }

    private void refreshShareFavsVisibility() {
        if (itemFav != null) {
            btnShareFavs.setVisibility(itemFav.isChecked() && adapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void configureDrawerLayout() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private View getTabView(int day) {
        View tabView = View.inflate(this, R.layout.view_tab, null);
        TextView tvDayWeek = (TextView) tabView.findViewById(R.id.tv_tab_day_week);
        TextView tvDayMonth = (TextView) tabView.findViewById(R.id.tv_tab_day_month);

        switch (day) {
            case 1:
                tvDayWeek.setText(R.string.friday_abrev);
                tvDayMonth.setText("2 " + getString(R.string.june));
                break;

            case 2:
                tvDayWeek.setText(R.string.saturday_abrev);
                tvDayMonth.setText("3 " + getString(R.string.june));
                break;

            case 3:
                tvDayWeek.setText(R.string.sunday_abrev);
                tvDayMonth.setText("4 " + getString(R.string.june));
                break;

        }
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
        }
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
            // Little trick to avoid animation when searching
            animationAdapter.setDuration(0);
            adapter.updateData(events);
            recyclerEvents.post(new Runnable() {
                @Override
                public void run() {
                    animationAdapter.setDuration(AnimationAdapter.DEFAULT_DURATION);
                }
            });
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
    public BasePresenter getPresenter() {
        return presenter;
    }

}
