package com.triskelapps.alcalasuena.interactor;

import android.content.Context;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.triskelapps.alcalasuena.api.Api;
import com.triskelapps.alcalasuena.base.BaseInteractor;
import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.util.Util;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class SettingsInteractor extends BaseInteractor {



    public interface SettingsIntValueCallback {

        void onResponse(Integer version);

        void onError(String error);
    }

    public SettingsInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }

    public void getLastDataVersion(final SettingsIntValueCallback callback) {

        if (!Util.isConnected(context)) {
            return;
        }

        getApi().getLastDataVersion()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(actionTerminate)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(String version) {

                        try {
                            int versionInt = Integer.parseInt(version);
                            callback.onResponse(versionInt);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }


                    }
                });


    }

    private Api getApi() {
        return getApi(Api.class);
    }


}
