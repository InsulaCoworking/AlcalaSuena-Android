package com.triskelapps.alcalasuena.ui.venue_info;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.databinding.RowEventBinding;
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

        final List<Band> bands = event.getBands();
        if (bands == null) {
            FirebaseCrashlytics.getInstance().recordException(new Error("band is null in this event! (eventsVenueAdapter): " + event.toString()));
            return;
        }


        holder.binding.tvEventTime.setText(event.getTimeFormatted());
        holder.binding.imgStarred.setSelected(event.isStarred());

        holder.binding.tvEventVenue.setVisibility(View.GONE);


        Band band1 = bands.get(0);
        holder.binding.tvBandName.setText(band1.getName());
        holder.binding.tvBandGenre.setText(band1.getGenreOrTag());

        Picasso.get()
                .load(band1.getImageLogoUrlFull())
                .placeholder(R.mipmap.img_default_grid)
                .error(R.mipmap.img_default_grid)
                .transform(new CircleTransform())
                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                .into(holder.binding.imgBand);

        if (band1.getTag() != null) {
            int color = Color.parseColor(band1.getTag().getColor());
            holder.binding.viewPointGenreColor.setColorFilter(color);
        }

        if (bands.size() > 1) {
            Band band2 = bands.get(1);
            holder.binding.tvBandName2.setVisibility(View.VISIBLE);
            holder.binding.viewGenre2.setVisibility(View.VISIBLE);

            holder.binding.tvBandName2.setText(band2.getName());
            holder.binding.tvBandGenre2.setText(band2.getGenreOrTag());

            if (band2.getTag() != null) {
                int color = Color.parseColor(band2.getTag().getColor());
                holder.binding.viewPointGenreColor2.setColorFilter(color);
            }
        } else {
            holder.binding.tvBandName2.setVisibility(View.GONE);
            holder.binding.viewGenre2.setVisibility(View.GONE);
        }

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

        RowEventBinding binding;

        public ViewHolder(View itemView) {

            super(itemView);

            binding = RowEventBinding.bind(itemView);

            binding.getRoot().setOnClickListener(v -> {

                if (itemClickListener != null) {
                    final Event event = getItemAtPosition(getAdapterPosition());
                    itemClickListener.onEventClicked(event);
                }
            });

            binding.imgStarred.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    final Event event = getItemAtPosition(getAdapterPosition());
                    binding.imgStarred.setSelected(!binding.imgStarred.isSelected());
                    itemClickListener.onEventFavouriteClicked(event.getId());
                }
            });
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onEventClicked(Event event);

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
