package com.triskelapps.alcalasuena.ui.venues;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.model.Venue;

import java.util.List;

/**
 * Created by julio on 7/07/16.
 */
public class VenuesAdapter extends RecyclerView.Adapter<VenuesAdapter.ViewHolder> {


    private List<Venue> venues;
    private Context context;
    private OnItemClickListener itemClickListener;

    private Integer selectedNumber = -1;


    public VenuesAdapter(Context context, List<Venue> venues) {
        this.context = context;
        this.venues = venues;
    }

    public void setSelectedNumber(Integer number) {
        this.selectedNumber = number;
        notifyDataSetChanged();
    }

    public Integer getSelectedNumber() {
        return selectedNumber;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(context).inflate(R.layout.grid_item_venue, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Venue venue = getItemAtPosition(position);

        holder.tvVenueName.setText(venue.getName());
        Picasso.with(context)
                .load(venue.getImageUrlFull())
//                .placeholder(R.mipmap.img_default_grid)
//                .error(R.mipmap.img_default_grid)
                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                .into(holder.imgVenue);


        addClickListener(holder.rootView, position, venue.getId());
    }

    private void addClickListener(View view, final int position, final int id) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, position, id);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return venues.size();
    }

    public Venue getItemAtPosition(int position) {
        return venues.get(position);
    }

    public void updateData(List<Venue> venues) {
        this.venues = venues;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvVenueName;
        public ImageView imgVenue;
        public View rootView;

        public ViewHolder(View itemView) {

            super(itemView);

            tvVenueName = (TextView) itemView.findViewById(R.id.tv_venue_name);
            imgVenue = (ImageView) itemView.findViewById(R.id.img_venue);

            rootView = itemView;
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int id);
    }
}
