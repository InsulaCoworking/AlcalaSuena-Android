package com.triskelapps.alcalasuena.ui.news;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.News;

import java.util.List;

public class NewsActivity extends BaseActivity implements NewsView, NewsAdapter.OnItemClickListener {

    private NewsPresenter presenter;
    private NewsAdapter adapter;
    private RecyclerView recyclerNews;

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
    }

    @Override
    public void onItemClick(View view, int position, int id) {
        presenter.onNewsClicked(id);
    }
}
