package com.triskelapps.alcalasuena.interactor;

import android.content.Context;

import com.google.gson.Gson;
import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.BuildConfig;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.api.Api;
import com.triskelapps.alcalasuena.base.BaseInteractor;
import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.News;
import com.triskelapps.alcalasuena.model.notification.FirebasePush;
import com.triskelapps.alcalasuena.model.notification.FirebasePushData;
import com.triskelapps.alcalasuena.model.notification.FirebasePushNotification;
import com.triskelapps.alcalasuena.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        for (News news : newsList) {
            news.configureDatesTime();
        }

        App.getDB().newsDao().insertAll(newsList);
    }

    public void storeNewsIndividual(News news) {
        List<News> newsList = new ArrayList<>();
        newsList.add(news);
        storeNews(newsList);
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
                    RequestBody.create(new File(imagePath), MediaType.parse("image/*")));
        }
        params.put(News.TITLE, RequestBody.create(title, MediaType.parse("text/plain")));
        params.put(News.TEXT, RequestBody.create(text, MediaType.parse("text/plain")));
        params.put(News.BTN_TEXT, RequestBody.create(linkButtonText, MediaType.parse("text/plain")));
        params.put(News.BTN_LINK, RequestBody.create(link, MediaType.parse("text/plain")));
        params.put(News.NATIVE_SCREEN_CODE, RequestBody.create("-1", MediaType.parse("text/plain")));

        Calendar calendar = Calendar.getInstance();
        String startDate = News.datetimeNewsFormatApiPost.format(new Date(calendar.getTimeInMillis()));

        calendar.add(Calendar.MONTH, 6);
        String endDate = News.datetimeNewsFormatApiPost.format(new Date(calendar.getTimeInMillis()));

        params.put(News.START_DATE_POPUP, RequestBody.create(startDate, MediaType.parse("text/plain")));
        params.put(News.END_DATE_POPUP, RequestBody.create(endDate, MediaType.parse("text/plain")));
        params.put(News.CADUCITY_DATE, RequestBody.create(endDate, MediaType.parse("text/plain")));

        params.put("api_key", RequestBody.create(context.getString(R.string.api_key), MediaType.parse("text/plain")));


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
        firebasePush.setTo("/topics/" + (BuildConfig.DEBUG ? App.TOPIC_NEWS_TEST : App.TOPIC_NEWS));
        firebasePush.setNotification(new FirebasePushNotification(title,  text));

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
