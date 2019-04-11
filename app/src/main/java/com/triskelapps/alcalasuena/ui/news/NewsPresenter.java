package com.triskelapps.alcalasuena.ui.news;

import android.content.Context;
import android.content.Intent;

import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.NewsInteractor;
import com.triskelapps.alcalasuena.model.News;
import com.triskelapps.alcalasuena.ui.news_info.NewsInfoPresenter;
import com.triskelapps.alcalasuena.util.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by julio on 2/06/17.
 */


 public class NewsPresenter extends BasePresenter {

     private final NewsView view;
    private final NewsInteractor newsInteractor;
    private List<News> news = new ArrayList<>();

    public static Intent newNewsActivity(Context context) {

         Intent intent = new Intent(context, NewsActivity.class);

         return intent;
     }

     public static NewsPresenter newInstance(NewsView view, Context context) {

         return new NewsPresenter(view, context);

     }

     private NewsPresenter(NewsView view, Context context) {
         super(context, view);

         this.view = view;
         newsInteractor = new NewsInteractor(context, view);

     }

     public void onCreate() {

         refreshData();
     }

     public void onResume() {

     }

     public void refreshData() {

         showStoredNews();

//         view.showProgressDialog(context.getString(R.string.loading));
         newsInteractor.getNewsFromApi(new NewsInteractor.NewsCallback() {
             @Override
             public void onResponse(List<News> newsListReceived) {
                 showStoredNews();
             }

             private void filterCurrentYearNews(List<News> newsList) {

             }

             @Override
             public void onError(String error) {
                view.toast(error);
             }
         });

     }

    private void showStoredNews() {

        List<News> newsList = newsInteractor.getNewsFromDB();
        this.news.clear();

        // Filter current year news
        for (News newsItem : newsList) {
            try {
                Date dateNews = DateUtils.formatDateTimeApi.parse(newsItem.getStart_date());
                Date date2019 = DateUtils.formatDateApi.parse("2019-01-01");
                if (dateNews.after(date2019)) {
                    news.add(newsItem);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        view.showNewsList(news);
    }

    public void onNewsClicked(int idNews) {
        context.startActivity(NewsInfoPresenter.newNewsInfoActivity(context, idNews));
    }

 }
