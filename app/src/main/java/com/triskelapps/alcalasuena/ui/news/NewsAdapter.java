package com.triskelapps.alcalasuena.ui.news;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.model.News;

import java.util.List;

/**
 * Created by julio on 7/07/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {


    private List<News> newsList;
    private Context context;
    private OnItemClickListener itemClickListener;

    private Integer selectedNumber = -1;


    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(context).inflate(R.layout.row_news, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position2) {

        final News news = getItemAtPosition(holder.getAdapterPosition());

        holder.tvNewsTitle.setText(news.getTitle());
        holder.tvNewsText.setText(news.getText());
        Picasso.get()
                .load(news.getImageUrlFull())
                .placeholder(R.mipmap.img_default_grid)
//                .error(R.mipmap.img_default_grid)
                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                .into(holder.imgNews);


        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, holder.getAdapterPosition(), news.getId());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public News getItemAtPosition(int position) {
        return newsList.get(position);
    }

    public void updateData(List<News> newsList) {
        this.newsList = newsList;
//        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNewsText;
        public TextView tvNewsTitle;
        public ImageView imgNews;
        public View rootView;

        public ViewHolder(View itemView) {

            super(itemView);

            tvNewsTitle = (TextView) itemView.findViewById(R.id.tv_news_title);
            tvNewsText = (TextView) itemView.findViewById(R.id.tv_news_text);
            imgNews = (ImageView) itemView.findViewById(R.id.img_news);

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
