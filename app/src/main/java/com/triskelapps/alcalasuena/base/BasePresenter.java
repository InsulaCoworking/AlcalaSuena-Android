package com.triskelapps.alcalasuena.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.util.Util;


/**
 * Created by julio on 16/12/15.
 */
public class BasePresenter {

    public final String TAG = this.getClass().getSimpleName();

    public Context context;
    public BaseView baseView;

    public BasePresenter(Context context, BaseView view) {
        this.context = context;
        this.baseView = view;
    }

    public SharedPreferences getPrefs() {
        return App.getPrefs(context);
    }

    // -------- UTILS --------------
    protected boolean isConnected() {
        return Util.isConnected(context);
    }

    protected boolean isConnectedIfNotShowAlert() {

        boolean isConnected = isConnected();
        if (!isConnected) {
            baseView.alert(context.getString(R.string.no_connection));
        }

        return isConnected;
    }

    protected boolean isConnectedIfNotShowToast() {

        boolean isConnected = isConnected();
        if (!isConnected) {
            baseView.toast(context.getString(R.string.no_connection));
        }

        return isConnected;
    }

    public String getString(int stringId) {
        return context.getString(stringId);
    }

    public void finish() {
        ((Activity) context).finish();
    }

    public void setBaseView(BaseView baseView) {
        this.baseView = baseView;
    }




    // ------- COMMON METHODS --------------

    public App getApp() {
        return (App) context.getApplicationContext();
    }


}
