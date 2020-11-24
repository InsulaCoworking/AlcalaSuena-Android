package com.triskelapps.alcalasuena.ui.venue_info;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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


        final Venue venue = ((VenueInfoActivity) getActivity()).getVenue();


        Picasso.with(getActivity())
                .load(venue.getImageUrlFull())
                .resizeDimen(R.dimen.width_image_big, R.dimen.height_image_big)
                .into(imgVenue);
        tvVenueDescription.setText(venue.getDescription());


        layout.findViewById(R.id.btn_how_to_arrive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String destinationCoords = venue.getLatitude() + "," + venue.getLongitude();

                String uri = "geo:0,0?q=" + destinationCoords + " (" + venue.getName() + ")";

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
//                    toast(R.string.no_googlemaps_available);
                }
            }
        });

        return layout;
    }

}
