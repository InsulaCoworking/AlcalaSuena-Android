package com.triskelapps.alcalasuena.ui.band_info;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.model.SocialItem;

import java.util.List;

public class SocialItemsBandAdapter extends RecyclerView.Adapter<SocialItemsBandAdapter.ViewHolder> {


    private List<SocialItem> socialItems;
    private Context context;
    private OnItemClickListener itemClickListener;


    public SocialItemsBandAdapter(Context context, List<SocialItem> socialItems) {
        this.context = context;
        this.socialItems = socialItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(context).inflate(R.layout.item_social, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position2) {

        final SocialItem socialItem = getItemAtPosition(holder.getAdapterPosition());

        holder.imgSocialItem.setImageResource(socialItem.getIconId());

    }

    @Override
    public int getItemCount() {
        return socialItems.size();
    }

    public SocialItem getItemAtPosition(int position) {
        return socialItems.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgSocialItem;
        public View rootView;

        public ViewHolder(View itemView) {

            super(itemView);

            imgSocialItem = itemView.findViewById(R.id.img_social_item);

            rootView = itemView;

            rootView.setOnClickListener(v -> {

                SocialItem socialItem = getItemAtPosition(getAdapterPosition());
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(socialItem.getUrl())));
                } catch (ActivityNotFoundException e) {
                    FirebaseCrashlytics.getInstance().recordException(e);
                    Toast.makeText(context, context.getString(R.string.link_error), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}


