package com.triskelapps.alcalasuena.ui.news;

import android.content.Context;
import android.content.Intent;

import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.NewsInteractor;
import com.triskelapps.alcalasuena.model.News;
import com.triskelapps.alcalasuena.ui.news_info.NewsInfoPresenter;

import java.util.List;

/**
 * Created by julio on 2/06/17.
 */


 public class NewsPresenter extends BasePresenter {

     private final NewsView view;
    private final NewsInteractor newsInteractor;

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


             @Override
             public void onError(String error) {
                view.toast(error);
             }
         });

     }

    private void showStoredNews() {

        List<News> newsList = App.getDB().newsDao().getAllSortedOfYear(App.FEST_YEAR);
        view.showNewsList(newsList);
    }

    public void onNewsClicked(int idNews) {
        context.startActivity(NewsInfoPresenter.newNewsInfoActivity(context, idNews));
    }

 }
