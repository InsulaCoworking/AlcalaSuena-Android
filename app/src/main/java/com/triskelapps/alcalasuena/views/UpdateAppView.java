package com.triskelapps.alcalasuena.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.triskelapps.alcalasuena.BuildConfig;
import com.triskelapps.alcalasuena.R;


public class UpdateAppView extends FrameLayout implements View.OnClickListener, LifecycleObserver {

    private static final String TAG = "UpdateAppView";

    private TextView btnUpdateApp;
    private AppCompatImageView btnCloseUpdateAppView;
    private AppUpdateManager appUpdateManager;
    private AppUpdateInfo appUpdateInfo;

    public static final String TEMPLATE_URL_GOOGLE_PLAY_APP_HTTP = "https://play.google.com/store/apps/details?id=%s";
    public static final String TEMPLATE_URL_GOOGLE_PLAY_APP_DIRECT = "market://details?id=%s";
    private AppUpdateListener appUpdateListener;
    private boolean showView = true;

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

        appUpdateManager = AppUpdateManagerFactory.create(getContext());

        if (getContext() instanceof Activity) {
            checkUpdateAvailable();
        }
    }

    public void setCustomListener(AppUpdateListener appUpdateListener, boolean showView) {
        this.appUpdateListener = appUpdateListener;
        this.showView = showView;
        if (!showView) {
            setVisibility(GONE);
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

        if (appUpdateInfo != null && appUpdateInfo.updateAvailability()
                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
            AppUpdateOptions options = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                    .setAllowAssetPackDeletion(true).build();
            appUpdateManager.startUpdateFlow(appUpdateInfo, (Activity) getContext(), options);

        }

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_close_update_app_view:
                setVisibility(GONE);
                break;

            case R.id.btn_update_app:
                onUpdateVersionClick();
//                CountlyUtil.recordEvent("update_app_button_click");
                break;
        }

    }


    public void onUpdateVersionClick() {

        if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
            AppUpdateOptions options = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                    .setAllowAssetPackDeletion(true).build();

            appUpdateManager.startUpdateFlow(appUpdateInfo, (Activity) getContext(), options);
        } else {
            openGooglePlay();
        }

    }

    private void openGooglePlay() {

        String packageName = getContext().getPackageName().replace(".debug", "");
        String httpUrl = String.format(TEMPLATE_URL_GOOGLE_PLAY_APP_HTTP, packageName);
        String directUrl = String.format(TEMPLATE_URL_GOOGLE_PLAY_APP_DIRECT, packageName);

        Intent directPlayIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(directUrl));
        if (directPlayIntent.resolveActivity(getContext().getPackageManager()) != null) {
            getContext().startActivity(directPlayIntent);
        } else {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(httpUrl)));
        }
    }

    private void checkUpdateAvailable() {

        if (BuildConfig.DEBUG) {
            return;
        }

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        appUpdateInfo = task.getResult();
                        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                            if (showView) {
                                setVisibility(VISIBLE);
                            }

                            if (appUpdateListener != null) {
                                appUpdateListener.onUpdateAvailable();
                            }
                        }

                    } else {
                        Exception e = task.getException();
//                        CountlyUtil.recordHandledException(e);
                        e.printStackTrace();

                    }
                });

    }
    
    public interface AppUpdateListener {
        void onUpdateAvailable();
    }
}
