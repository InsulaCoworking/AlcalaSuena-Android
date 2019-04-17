package com.triskelapps.alcalasuena.interactor;

import android.content.Context;

import com.google.gson.Gson;
import com.triskelapps.alcalasuena.BuildConfig;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.api.Api;
import com.triskelapps.alcalasuena.base.BaseInteractor;
import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.News;
import com.triskelapps.alcalasuena.model.NewsState;
import com.triskelapps.alcalasuena.model.notification.FirebasePush;
import com.triskelapps.alcalasuena.model.notification.FirebasePushData;
import com.triskelapps.alcalasuena.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.Sort;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
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
        realm.delete(News.class);
        realm.insertOrUpdate(newsList);
        realm.commitTransaction();
    }

    public void storeNewsIndividual(News news) {
        List<News> newsList = new ArrayList<>();
        newsList.add(news);
        storeNews(newsList);
    }


    public List<News> getNewsFromDB() {
        return Realm.getDefaultInstance().where(News.class).findAll().sort(News.START_DATE_POPUP_TIME, Sort.DESCENDING);
    }

    public News getNewsById(int idNews) {
        return Realm.getDefaultInstance().where(News.class).equalTo(News.ID, idNews).findFirst();
    }


    public News getLastUnseenNews() {

        long currentTime = System.currentTimeMillis();

        News news = Realm.getDefaultInstance().where(News.class)
                .greaterThanOrEqualTo(News.END_DATE_POPUP_TIME, currentTime)
                .lessThanOrEqualTo(News.START_DATE_POPUP_TIME, currentTime)
                .findAll().sort(News.START_DATE_POPUP_TIME, Sort.DESCENDING).first();

        if (news == null) {
            return null;
        } else if (isNewsSeen(news.getId())) {
            return null;
        } else if (news.getId() == 1) {
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


    public void sendNews(String title, String text, String link, String linkButtonText, String imagePath, final BasePOSTFullEntityCallback<News> callback) {

        if (!Util.isConnected(context)) {
            return;
        }

        Map<String, RequestBody> params = new HashMap<>();
        MultipartBody.Part filePart = null;
        if (imagePath != null) {
            filePart = MultipartBody.Part.createFormData("image",
                    title.replace(" ", "-") + ".jpg",
                    RequestBody.create(MediaType.parse("image/*"), new File(imagePath)));
        }
        params.put(News.TITLE, RequestBody.create(MediaType.parse("text/plain"), title));
        params.put(News.TEXT, RequestBody.create(MediaType.parse("text/plain"), text));
        params.put(News.BTN_TEXT, RequestBody.create(MediaType.parse("text/plain"), linkButtonText));
        params.put(News.BTN_LINK, RequestBody.create(MediaType.parse("text/plain"), link));
        params.put(News.NATIVE_SCREEN_CODE, RequestBody.create(MediaType.parse("text/plain"), "-1"));

        Calendar calendar = Calendar.getInstance();
        String startDate = News.datetimeNewsFormatApiPost.format(new Date(calendar.getTimeInMillis()));

        calendar.add(Calendar.MONTH, 6);
        String endDate = News.datetimeNewsFormatApiPost.format(new Date(calendar.getTimeInMillis()));

        params.put(News.START_DATE_POPUP, RequestBody.create(MediaType.parse("text/plain"), startDate));
        params.put(News.END_DATE_POPUP, RequestBody.create(MediaType.parse("text/plain"), endDate));
        params.put(News.CADUCITY_DATE, RequestBody.create(MediaType.parse("text/plain"), endDate));

        params.put("api_key", RequestBody.create(MediaType.parse("text/plain"), context.getString(R.string.api_key)));


        getApi().sendNews(filePart, params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(actionTerminate)
                .subscribe(new Observer<Response<News>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<News> response) {

                        if (response.isSuccessful()) {
                            callback.onSuccess(response.body());
                        } else {
                            try {
                                callback.onError(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });
    }


    public void sendNewsNotification(String title, String text, String link, String linkButtonText, News news, final BasePOSTCallback callback) {

        FirebasePush firebasePush = new FirebasePush();
        firebasePush.setTo(BuildConfig.DEBUG ? "/topics/test_news" : "/topics/news");
//        firebasePush.setNotification(new FirebasePushNotification(title,  text));

        FirebasePushData firebaseData = new FirebasePushData();
        firebaseData.setTitle(title);
        firebaseData.setMessage(text);
        firebaseData.setBtn_link(link);
        firebaseData.setBtn_text(linkButtonText);
        firebaseData.setId_news(news != null ? String.valueOf(news.getId()) : null);
        if (news != null) {
            firebaseData.setNewsJson(new Gson().toJson(news));
        }
        firebasePush.setData(firebaseData);

        String serverKeyMod = context.getString(R.string.firebase_server_key);
        String serverKey = "key=" + serverKeyMod.substring(0, serverKeyMod.length()-4);

        getApi().sendPush(serverKey, firebasePush)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(actionTerminate)
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<Void> response) {

                        if (response.isSuccessful()) {
                            callback.onSuccess(null);
                        } else {
                            try {
                                callback.onError(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });
    }

    private Api getApi() {
        return getApi(Api.class);
    }


}
