package com.triskelapps.alcalasuena.ui.events;

import android.content.Context;
import android.graphics.Color;
import androidx.core.graphics.ColorUtils;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.databinding.RowEventBinding;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Event;
import com.triskelapps.alcalasuena.views.CircleTransform;

import java.util.List;

/**
 * Created by julio on 7/07/16.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {


    private List<Event> events;
    private Context context;
    private OnItemClickListener itemClickListener;

    private Integer selectedNumber = -1;


    public EventsAdapter(Context context, List<Event> events) {
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
    public void onBindViewHolder(final ViewHolder holder, final int position2) {

        final Event event = getItemAtPosition(holder.getAdapterPosition());

        final List<Band> bands = event.getBands();
        if (bands == null) {
            FirebaseCrashlytics.getInstance().recordException(new Error("band is null in this event! (eventsVenueAdapter): " + event.toString()));
            return;
        }

        holder.binding.tvEventTime.setText(event.getTimeFormatted());
        holder.binding.tvEventVenue.setText(event.getVenue().getName());
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
//        notifyDataSetChanged();
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
}
