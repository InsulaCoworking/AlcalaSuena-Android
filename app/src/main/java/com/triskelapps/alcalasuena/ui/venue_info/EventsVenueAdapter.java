package com.triskelapps.alcalasuena.ui.venue_info;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.views.CircleTransform;

import java.util.List;

import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter;

/**
 * Created by julio on 7/07/16.
 */
public class EventsVenueAdapter extends RecyclerView.Adapter<EventsVenueAdapter.ViewHolder>
        implements StickyHeaderAdapter<EventsVenueAdapter.HeaderHolder> {


    private List<Event> events;
    private Context context;
    private OnItemClickListener itemClickListener;

    private Integer selectedNumber = -1;
    private int hightlightPosition = -1;


    public EventsVenueAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(context).inflate(R.layout.row_event, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Event event = getItemAtPosition(position);

        final Band band = event.getBandEntity();
        if (band == null) {
            Crashlytics.logException(new Error("band is null in this event! (eventsVenueAdapter): " + event.toString()));
            return;
        }

        holder.tvBandName.setText(band.getName());
        holder.tvBandGenre.setText(band.getGenreOrTag());
        Picasso.with(context)
                .load(band.getImageLogoUrlFull())
                .placeholder(R.mipmap.img_default_grid)
                .error(R.mipmap.img_default_grid)
                .transform(new CircleTransform())
                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                .into(holder.imgBand);

        holder.tvEventTime.setText(event.getTimeFormatted());
        holder.tvEventVenue.setText(event.getVenue().getName());

        holder.tvEventVenue.setVisibility(View.INVISIBLE);

        holder.rootView.setSelected(hightlightPosition == position);

        holder.imgStarred.setSelected(event.isStarred());

        if (band.getTag() != null) {
            int color = Color.parseColor(band.getTag().getColor());
            int colorAlpha = ColorUtils.setAlphaComponent(color, 230);
            holder.cardEvent.setCardBackgroundColor(colorAlpha);
        }

//        int color = ContextCompat.getColor(context, event.getImageLogoUrlFull() != null ? android.R.color.white : android.R.color.black);
//        holder.tvEventName.setTextColor(color);
//        holder.tvEventGenre.setTextColor(color);


    }

    private void addClickListener(View view, final int position) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (itemClickListener != null) {
//                    itemClickListener.onItemClick(v, position);
//                }
            }
        });
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

    public void setHightlightPosition(int hightlightPosition) {
        this.hightlightPosition = hightlightPosition;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardEvent;
        private ImageView imgBand;
        private TextView tvBandName;
        private TextView tvBandGenre;
        private ImageView imgStarred;
        private TextView tvEventVenue;
        private TextView tvEventTime;
        public View rootView;

        public ViewHolder(View itemView) {

            super(itemView);

            cardEvent = (CardView)itemView.findViewById( R.id.card_event );
            imgBand = (ImageView)itemView.findViewById( R.id.img_band );
            tvBandName = (TextView)itemView.findViewById( R.id.tv_band_name );
            tvBandGenre = (TextView)itemView.findViewById( R.id.tv_band_genre );
            imgStarred = (ImageView)itemView.findViewById( R.id.img_starred );
            tvEventVenue = (TextView)itemView.findViewById( R.id.tv_event_venue );
            tvEventTime = (TextView)itemView.findViewById( R.id.tv_event_time );

            rootView = itemView;


            rootView.setOnClickListener(v -> {

                if (itemClickListener != null) {
                    final Event event = getItemAtPosition(getAdapterPosition());
                    final Band band = event.getBandEntity();
                    itemClickListener.onBandClicked(band.getId());
                }
            });

            imgStarred.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    final Event event = getItemAtPosition(getAdapterPosition());
                    imgStarred.setSelected(!imgStarred.isSelected());
                    itemClickListener.onEventFavouriteClicked(event.getId());
                }
            });
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onBandClicked(int idBand);

        void onEventFavouriteClicked(int idEvent);

    }


    @Override
    public long getHeaderId(int position) {
        return events.get(position).getDayId();
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {

        View headerView = LayoutInflater.from(context).inflate(R.layout.row_event_venue_header, parent, false);

        // Return a new holder instance
        HeaderHolder viewHolder = new HeaderHolder(headerView);
        return viewHolder;
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder viewholder, int position) {

        Event event = events.get(position);
        viewholder.tvHeaderDay.setText(event.getDayShareFormat());
    }


    public class HeaderHolder extends RecyclerView.ViewHolder {

        private final TextView tvHeaderDay;

        public HeaderHolder(View headerView) {
            super(headerView);
            tvHeaderDay = (TextView) headerView.findViewById(R.id.tv_header_day);
        }
    }
}
