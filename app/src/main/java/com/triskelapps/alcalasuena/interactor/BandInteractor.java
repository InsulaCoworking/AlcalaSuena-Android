package com.triskelapps.alcalasuena.interactor;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.api.Api;
import com.triskelapps.alcalasuena.base.BaseInteractor;
import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Tag;
import com.triskelapps.alcalasuena.model.TagState;
import com.triskelapps.alcalasuena.util.Util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class BandInteractor extends BaseInteractor {


    public interface BandsCallback {

        void onResponse(List<Band> bands);

        void onError(String error);
    }

    public BandInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }

    public void initializeBandsFirstTime() {
        Log.i(TAG, "initializeBandsFirstTime: start");

        final Gson gson = new GsonBuilder().create();

        try {

            final String jsonTotal = Util.getStringFromAssets(context, "bands.json");
            Type listType = new TypeToken<List<Band>>() {}.getType();
            final List<Band> bands = gson.fromJson(jsonTotal, listType);
            storeBands(bands);

            final String jsonTags = Util.getStringFromAssets(context, "tags.json");
            Type listTypeTags = new TypeToken<List<Tag>>() {}.getType();
            final List<Tag> tags = gson.fromJson(jsonTags, listTypeTags);
            storeTags(tags);

            Log.i(TAG, "initializeBandsFirstTime: end");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void getBands(final BandsCallback callback) {

        if (!Util.isConnected(context)) {
//            baseView.toast(R.string.no_connection);
            return;
        }

        getApi().getBands()
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
                .subscribe(new Observer<List<Band>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Band> bands) {

                        storeBands(bands);

                        callback.onResponse(bands);


                    }
                });


    }

    private void storeBands(List<Band> bands) {
        App.getDB().bandDao().deleteAll();
        bands.stream().forEach(band -> band.setIdTag(band.getTag().getId()));
        App.getDB().bandDao().insertAll(bands);
    }

    private void storeTags(List<Tag> tags) {
        App.getDB().tagDao().deleteAll();
        App.getDB().tagDao().insertAll(tags);


        List<TagState> tagsState = tags.stream().map(tag -> new TagState(tag.getId())).collect(Collectors.toList());
        App.getDB().tagStateDao().deleteAll();
        App.getDB().tagStateDao().insertAll(tagsState);
    }

    private Api getApi() {
        return getApi(Api.class);
    }


}
