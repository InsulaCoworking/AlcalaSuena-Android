package com.triskelapps.alcalasuena.ui.bands;

import android.content.Context;
import android.graphics.Color;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Band band = getItemAtPosition(holder.getAdapterPosition());

        holder.tvBandName.setText(band.getName());
        holder.tvBandGenre.setText(band.getGenreOrTag());
        Picasso.get()
                .load(band.getImageCoverUrlFull())
//                .placeholder(R.mipmap.img_default_grid)
//                .error(R.mipmap.img_default_grid)
                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                .into(holder.imgBand);

        if (band.getTag() != null) {
            int color = Color.parseColor(band.getTag().getColor());
            int colorAlpha = ColorUtils.setAlphaComponent(color, 200);
            holder.viewColorTag.setBackgroundColor(colorAlpha);
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, holder.getAdapterPosition(), band.getId());
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
//        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View viewColorTag;
        public TextView tvBandName;
        public TextView tvBandGenre;
        public ImageView imgBand;
        public View rootView;

        public ViewHolder(View itemView) {

            super(itemView);

            tvBandName = (TextView) itemView.findViewById(R.id.tv_band_name);
            tvBandGenre = (TextView) itemView.findViewById(R.id.tv_band_genre);
            imgBand = (ImageView) itemView.findViewById(R.id.img_band);
            viewColorTag = itemView.findViewById(R.id.view_color_tag);

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
