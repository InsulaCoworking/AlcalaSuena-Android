package com.triskelapps.alcalasuena.ui.venue_info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.model.Event;

import java.util.List;

import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;

/**
 * Created by julio on 29/05/17.
 */

public class VenueInfoEventsFragment extends Fragment {

    private RecyclerView recyclerEventsVenue;
    private EventsVenueAdapter adapter;

    public VenueInfoEventsFragment() {
        // Required empty public constructor
    }

    public static VenueInfoEventsFragment newInstance() {
        VenueInfoEventsFragment fragment = new VenueInfoEventsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_venue_info_events, container, false);

        recyclerEventsVenue = (RecyclerView) layout.findViewById(R.id.recycler_events_venue);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerEventsVenue.setLayoutManager(layoutManager);

        List<Event> events = ((VenueInfoActivity) getActivity()).getEventsVenue();

        if (adapter == null) {

            adapter = new EventsVenueAdapter(getActivity(), events);
            adapter.setOnItemClickListener((VenueInfoActivity) getActivity());

            // https://github.com/edubarr/header-decor
            StickyHeaderDecoration decor = new StickyHeaderDecoration(adapter);

            recyclerEventsVenue.setAdapter(adapter);
            recyclerEventsVenue.addItemDecoration(decor);

        } else {
            adapter.updateData(events);
        }

        return layout;
    }

}
