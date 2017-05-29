package com.triskelapps.alcalasuena.ui.venue_info;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.model.Venue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VenueInfoMeetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VenueInfoMeetFragment extends Fragment {

    public VenueInfoMeetFragment() {
        // Required empty public constructor
    }

    public static VenueInfoMeetFragment newInstance() {
        VenueInfoMeetFragment fragment = new VenueInfoMeetFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_venue_info_meet, container, false);


        ImageView imgVenue = (ImageView) layout.findViewById(R.id.img_venue);
        TextView tvVenueDescription = (TextView) layout.findViewById(R.id.tv_venue_description);

        Venue venue = ((VenueInfoActivity) getActivity()).getVenue();


        Picasso.with(getActivity())
                .load(venue.getImageUrlFull())
                .into(imgVenue);
        tvVenueDescription.setText(venue.getDescription());

        return layout;
    }

}
