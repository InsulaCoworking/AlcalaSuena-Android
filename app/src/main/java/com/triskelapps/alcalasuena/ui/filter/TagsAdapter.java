package com.triskelapps.alcalasuena.ui.filter;

import android.content.Context;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.model.Tag;

import java.util.List;

/**
 * Created by julio on 7/07/16.
 */
public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {


    private List<Tag> tags;
    private Context context;
    private OnItemClickListener itemClickListener;

    public TagsAdapter(Context context, List<Tag> tags) {
        this.context = context;
        this.tags = tags;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(context).inflate(R.layout.grid_item_tag, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position2) {

        Tag tag = getItemAtPosition(holder.getAdapterPosition());

        holder.tvTagName.setText(tag.getName());
//        holder.tvTagDescription.setText(tag.getDescription());

        int color = Color.parseColor(tag.getColor());
//        int colorAlpha = ColorUtils.setAlphaComponent(color, 255);
        holder.cardTag.setCardBackgroundColor(color);

        holder.rootView.setAlpha(tag.isActive() ? 1 : 0.4f);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, holder.getAdapterPosition(), getItemAtPosition(holder.getAdapterPosition()).getId());
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return tags.size();
    }

    public Tag getItemAtPosition(int position) {
        return tags.get(position);
    }

    public void updateData(List<Tag> tags) {
        this.tags = tags;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardTag;
        public TextView tvTagName;
//        public TextView tvTagDescription;
        public View rootView;

        public ViewHolder(View itemView) {

            super(itemView);

            tvTagName = (TextView) itemView.findViewById(R.id.tv_tag_name);
//            tvTagDescription = (TextView) itemView.findViewById(R.id.tv_tag_description);
            cardTag = (CardView) itemView.findViewById(R.id.card_tag);

            rootView = itemView;
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, String id);
    }
}
