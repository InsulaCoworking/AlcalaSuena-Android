package com.triskelapps.alcalasuena.interactor;

import android.content.Context;

import com.triskelapps.alcalasuena.api.Api;
import com.triskelapps.alcalasuena.base.BaseInteractor;
import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.News;
import com.triskelapps.alcalasuena.model.NewsState;
import com.triskelapps.alcalasuena.util.Util;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class NewsInteractor extends BaseInteractor {



    public interface NewsCallback {

        void onResponse(List<News> newsList);

        void onError(String error);
    }

    public NewsInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }

    public void getNewsFromApi(final NewsCallback callback) {

        if (!Util.isConnected(context)) {
            return;
        }

        getApi().getNews()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(actionTerminate)
                .subscribe(new Observer<List<News>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<News> newsList) {

                        storeNews(newsList);
                        callback.onResponse(newsList);


                    }
                });


    }

    private void storeNews(List<News> newsList) {
        Realm realm = Realm.getDefaultInstance();

//        realm.beginTransaction();
//        realm.where(News.class).findAll().deleteAllFromRealm();
//        realm.commitTransaction();

        for (News news : newsList) {
            news.configureDatesTime();
        }

        realm.beginTransaction();
        realm.insertOrUpdate(newsList);
        realm.commitTransaction();
    }


    public List<News> getNewsFromDB() {
        return Realm.getDefaultInstance().where(News.class).findAll().sort(News.START_DATE_POPUP_TIME, Sort.DESCENDING);
    }

    public News getNewsById(int idNews) {
        return Realm.getDefaultInstance().where(News.class).equalTo(News.ID, idNews).findFirst();
    }


    public News getLastUnseenNews() {

        long currentTime = System.currentTimeMillis();

        News news =  Realm.getDefaultInstance().where(News.class)
                .greaterThanOrEqualTo(News.END_DATE_POPUP_TIME, currentTime)
                .lessThanOrEqualTo(News.START_DATE_POPUP_TIME, currentTime)
                .findAll().sort(News.START_DATE_POPUP_TIME, Sort.DESCENDING).first();

        if (isNewsSeen(news.getId())) {
            return null;
        }

        return news;


    }

    private boolean isNewsSeen(int idNews) {
        return Realm.getDefaultInstance().where(NewsState.class).equalTo(NewsState.ID_NEWS, idNews).findFirst() != null;
    }


    public void setNewsSeen(final int idNews) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        NewsState newsState = realm.createObject(NewsState.class);
        newsState.setIdNews(idNews);
        newsState.setSeen(true);
        realm.commitTransaction();
    }

    private Api getApi() {
        return getApi(Api.class);
    }


}
