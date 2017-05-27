package com.triskelapps.alcalasuena.ui.bands;

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

import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.model.Band;

import java.util.List;

/**
 * Created by julio on 7/07/16.
 */
public class BandsAdapter extends RecyclerView.Adapter<BandsAdapter.ViewHolder> {


    private List<Band> bands;
    private Context context;
    private OnItemClickListener itemClickListener;

    private Integer selectedNumber = -1;


    public BandsAdapter(Context context, List<Band> bands) {
        this.context = context;
        this.bands = bands;
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

        View contactView = LayoutInflater.from(context).inflate(R.layout.grid_item_band, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Band band = getItemAtPosition(position);

        holder.tvBandName.setText(band.getName());
        holder.tvBandGenre.setText(band.getGenre());
        holder.tvBandDescription.setText(band.getDescription());
        Picasso.with(context)
                .load(band.getImageUrlFull())
                .placeholder(R.mipmap.img_default_grid)
                .error(R.mipmap.img_default_grid)
                .into(holder.imgBand);

        if (band.getTag() != null) {
            int color = Color.parseColor(band.getTag().getColor());
            int colorAlpha = ColorUtils.setAlphaComponent(color, 150);
            holder.cardBand.setCardBackgroundColor(colorAlpha);
        }

        addClickListener(holder.rootView, position, band.getId());
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
        return bands.size();
    }

    public Band getItemAtPosition(int position) {
        return bands.get(position);
    }

    public void updateData(List<Band> bands) {
        this.bands = bands;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvBandName;
        public TextView tvBandGenre;
        public TextView tvBandDescription;
        public ImageView imgBand;
        public CardView cardBand;
        public View rootView;

        public ViewHolder(View itemView) {

            super(itemView);

            tvBandName = (TextView) itemView.findViewById(R.id.tv_band_name);
            tvBandGenre = (TextView) itemView.findViewById(R.id.tv_band_genre);
            tvBandDescription = (TextView) itemView.findViewById(R.id.tv_band_description);
            cardBand = (CardView) itemView.findViewById(R.id.card_band);
            imgBand = (ImageView) itemView.findViewById(R.id.img_band);

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
