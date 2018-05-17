package com.triskelapps.alcalasuena.base;

import android.content.Context;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.api.common.ApiClient;
import com.triskelapps.alcalasuena.util.Util;

import java.util.List;

import rx.functions.Action0;

/**
 * Created by julio on 14/02/16.
 */
public class BaseInteractor {

    public final String TAG = this.getClass().getSimpleName();

    public Context context;
    public BaseView baseView;

    public <T> T getApi(Class<T> service) {
        return ApiClient.getInstance().create(service);
    }

    public interface BaseCallback {
        void onSuccess();

        void onError(String message);
    }

    public interface BaseElementCallback<T> {
        void onSuccess(T element);

        void onError(String message);
    }

    public interface BaseListCallback<T> {
        void onSuccess(List<T> list);

        void onError(String message);
    }


    public interface BasePOSTFullEntityCallback<T> {
        void onSuccess(T entity);

        void onError(String message);
    }

    public interface BasePOSTCallback {
        void onSuccess(Integer id);

        void onError(String message);
    }

    public Action0 actionTerminate = new Action0() {
        @Override
        public void call() {

            if (baseView != null) {
                baseView.setRefresing(false);
                baseView.hideProgressDialog();
            }

        }
    };


    public boolean isConnected() {

        boolean connected = Util.isConnected(context);

        if (!connected) {
            if (baseView != null) {
                baseView.toast(R.string.no_connection);
            }
        }

        return connected;
    }

}
