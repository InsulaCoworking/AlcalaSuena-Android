package com.triskelapps.alcalasuena.util.update_app;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.triskelapps.alcalasuena.BuildConfig;
import com.triskelapps.alcalasuena.R;


public class UpdateAppView extends FrameLayout implements View.OnClickListener, LifecycleObserver {

    private static final String TAG = "UpdateAppView";

    private TextView btnUpdateApp;
    private AppCompatImageView btnCloseUpdateAppView;

    private UpdateAppManager updateAppManager;

    public UpdateAppView(Context context) {
        super(context);
        init();
    }


    public UpdateAppView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UpdateAppView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void findViews(View layout) {
        btnUpdateApp = (TextView) layout.findViewById(R.id.btn_update_app);
        btnCloseUpdateAppView = (AppCompatImageView) layout.findViewById(R.id.btn_close_update_app_view);

        btnUpdateApp.setOnClickListener(this);
        btnCloseUpdateAppView.setOnClickListener(this);
    }

    private void init() {
        View layout = View.inflate(getContext(), R.layout.view_update_app, null);
        findViews(layout);

        addView(layout);

        setVisibility(GONE);

        configure();
    }

    private void configure() {

        updateAppManager = new UpdateAppManager(getContext());
        updateAppManager.setUpdateAvailableListener(() -> setVisibility(VISIBLE));

        if (getContext() instanceof Activity) {
            checkUpdateAvailable();
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, "onAttachedToWindow: ");
        if (getContext() instanceof AppCompatActivity) {
            ((AppCompatActivity) getContext()).getLifecycle().addObserver(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.i(TAG, "onDetachedFromWindow: ");
        super.onDetachedFromWindow();
        if (getContext() instanceof AppCompatActivity) {
            ((AppCompatActivity) getContext()).getLifecycle().removeObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void checkUpdateInProgress() {
        Log.i(TAG, "checkUpdateInProgress: ");

        if (BuildConfig.DEBUG) {
            return;
        }

        updateAppManager.onResume();

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_close_update_app_view:
                setVisibility(GONE);
                break;

            case R.id.btn_update_app:
                updateAppManager.onUpdateVersionClick();
//                CountlyUtil.recordEvent("update_app_button_click");
                break;
        }

    }



    private void checkUpdateAvailable() {

        if (BuildConfig.DEBUG) {
            return;
        }

        updateAppManager.checkUpdateAvailable();

    }

}
