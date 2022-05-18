package com.triskelapps.alcalasuena.ui.band_info;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.model.Event;

import java.util.List;

/**
 * Created by julio on 7/07/16.
 */
public class EventsBandAdapter extends RecyclerView.Adapter<EventsBandAdapter.ViewHolder> {


    private List<Event> events;
    private Context context;
    private OnItemClickListener itemClickListener;

    private Integer selectedNumber = -1;


    public EventsBandAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(context).inflate(R.layout.row_event_band, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position2) {

        final Event event = getItemAtPosition(holder.getAdapterPosition());


        holder.tvEventBandDay.setText(event.getDayShareFormat());
        holder.tvEventBandTime.setText(event.getTimeFormatted());
        holder.tvEventBandVenue.setText(event.getVenue().getName());
        holder.tvEventBandDuration.setText(event.getDurationFormatted(context));

        holder.imgStarred.setSelected(event.isStarred());

    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public Event getItemAtPosition(int position) {
        return events.get(position);
    }

    public void updateData(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {	private TextView tvEventBandDay;
        private TextView tvEventBandTime;
        private TextView tvEventBandVenue;
        private ImageView imgStarred;
        private TextView tvEventBandDuration;
        public View rootView;

        public ViewHolder(View itemView) {

            super(itemView);

            tvEventBandDay = (TextView)itemView.findViewById( R.id.tv_event_band_day );
            tvEventBandTime = (TextView)itemView.findViewById( R.id.tv_event_band_time );
            tvEventBandVenue = (TextView)itemView.findViewById( R.id.tv_event_band_venue );
            imgStarred = (ImageView)itemView.findViewById( R.id.img_starred );
            tvEventBandDuration = (TextView)itemView.findViewById( R.id.tv_event_band_duration );

            rootView = itemView;

            imgStarred.setOnClickListener(v ->
                    itemClickListener.onEventFavouriteClicked(getItemAtPosition(getAdapterPosition()).getId()));

            tvEventBandVenue.setOnClickListener(v ->
                    itemClickListener.onEventVenueClicked(getItemAtPosition(getAdapterPosition()).getId()));
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {

        void onEventFavouriteClicked(int idEvent);
        void onEventVenueClicked(int idEvent);

    }
}
