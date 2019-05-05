package com.triskelapps.alcalasuena.ui.news_info;

import android.content.Context;
import android.content.Intent;

import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.NewsInteractor;
import com.triskelapps.alcalasuena.model.News;

import io.realm.Realm;

/**
 * Created by julio on 2/06/17.
 */


public class NewsInfoPresenter extends BasePresenter {

    private static final String EXTRA_ID_NEWS = "extra_id_news";

    private final NewsInfoView view;
    private final NewsInteractor newsInteractor;
    private int idNews;

    public static Intent newNewsInfoActivity(Context context, int idNews) {

        Intent intent = new Intent(context, NewsInfoActivity.class);
        intent.putExtra(EXTRA_ID_NEWS, idNews);
        return intent;
    }

    public static NewsInfoPresenter newInstance(NewsInfoView view, Context context) {

        return new NewsInfoPresenter(view, context);

    }

    private NewsInfoPresenter(NewsInfoView view, Context context) {
        super(context, view);

        this.view = view;
        newsInteractor = new NewsInteractor(context, view);
    }

    public void onCreate(Intent intent) {

        idNews = intent.getIntExtra(EXTRA_ID_NEWS, -1);
        if (idNews == -1) {
            throw new IllegalArgumentException("NewsInfo need an idNew argument");
        }
        refreshData();
    }

    public void onResume() {

    }

    public void refreshData() {

        News news = newsInteractor.getNewsById(idNews);
        News newsCopy = Realm.getDefaultInstance().copyFromRealm(news);
        view.showNews(newsCopy);

    }

}
