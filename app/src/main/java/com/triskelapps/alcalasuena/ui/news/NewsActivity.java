package com.triskelapps.alcalasuena.ui.news;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.News;

import java.util.List;

public class NewsActivity extends BaseActivity implements NewsView, NewsAdapter.OnItemClickListener {

    private NewsPresenter presenter;
    private NewsAdapter adapter;
    private RecyclerView recyclerNews;
    private TextView tvEmptyList;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = NewsPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        configureSecondLevelActivity();
        setToolbarTitle(R.string.news);

        recyclerNews = (RecyclerView)findViewById(R.id.recycler_news);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerNews.setLayoutManager(layoutManager);

        tvEmptyList = (TextView) findViewById(R.id.tv_empty_list_news);

        presenter.onCreate();
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showNewsList(List<News> newsList) {

        if (adapter == null) {

            adapter = new NewsAdapter(this, newsList);
            adapter.setOnItemClickListener(this);

            recyclerNews.setAdapter(adapter);

        } else {
            adapter.updateData(newsList);
            recyclerNews.getRecycledViewPool().clear();
            adapter.notifyDataSetChanged();
        }

        tvEmptyList.setVisibility(newsList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(View view, int position, int id) {
        presenter.onNewsClicked(id);
    }
}
