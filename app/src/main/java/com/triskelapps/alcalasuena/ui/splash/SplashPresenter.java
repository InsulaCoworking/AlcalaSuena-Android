package com.triskelapps.alcalasuena.ui.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.triskelapps.alcalasuena.BuildConfig;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.ui.MainPresenter;
import com.triskelapps.alcalasuena.ui.about.AboutAlcalaSuenaActivity;
import com.triskelapps.alcalasuena.ui.intro.IntroPresenter;
import com.triskelapps.alcalasuena.util.Util;
import com.triskelapps.alcalasuena.util.WebUtils;
import com.triskelapps.alcalasuena.util.update_app.UpdateAppManager;

/**
 * Created by julio on 29/05/17.
 */


public class SplashPresenter extends BasePresenter {

    public static final int NEXT_SCREEN_INTRO = 0;
    public static final int NEXT_SCREEN_ABOUT = 1;
    public static final int NEXT_SCREEN_MAIN = 2;
    public static final int NEXT_SCREEN_NONE = -1;

    private int nextScreen;

    private final SplashView view;
    private Handler handler;
    private UpdateAppManager updateAppManager;

    public static void launchSplashActivity(Context context, int nextScreen) {

        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra(Util.EXTRA_INT, nextScreen);
        context.startActivity(intent);
    }

    public static SplashPresenter newInstance(SplashView view, Context context) {

        return new SplashPresenter(view, context);

    }

    private SplashPresenter(SplashView view, Context context) {
        super(context, view);

        this.view = view;

    }


    private Runnable runnableNextScreen = () -> {
        // If connection failed, launch it anyway
        launchNextScreen();
    };

    public void onCreate(Intent intent) {

        if (intent.hasExtra(Util.EXTRA_INT)) {
            nextScreen = intent.getIntExtra(Util.EXTRA_INT, -1);
        } else {
            throw new IllegalArgumentException("Next screen parameter needed");
        }

        if (nextScreen == NEXT_SCREEN_NONE) {
            view.showTvInfoText(getString(R.string.preparing_app_edition_current_year), false);

            updateAppManager = new UpdateAppManager(context);
            updateAppManager.setUpdateAvailableListener(() -> onUpdateAvailable());
            updateAppManager.checkUpdateAvailable();
        }

        handler = new Handler(Looper.getMainLooper());
    }


    public void onResume() {

        handler.postDelayed(runnableNextScreen, 3000);

        if (updateAppManager != null) {
            updateAppManager.onResume();
        }
    }

    public void onPause() {
        handler.removeCallbacks(runnableNextScreen);
    }


    private void launchNextScreen() {

        switch (nextScreen) {
            case NEXT_SCREEN_INTRO:
                context.startActivity(IntroPresenter.newIntroActivity(context));
                break;

            case NEXT_SCREEN_ABOUT:
                context.startActivity(new Intent(context, AboutAlcalaSuenaActivity.class));
                break;

            case NEXT_SCREEN_MAIN:
                context.startActivity(MainPresenter.newMainActivity(context));
                break;

            case NEXT_SCREEN_NONE:
                return;
        }

        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(0, 0);
    }


    public void onSplashImageClick() {
        if (BuildConfig.MODE_PREPARING) {
            WebUtils.openCustomTab(context, "https://alcalasuena.es");
        }
    }

    public void onUpdateAvailable() {
        if (nextScreen == NEXT_SCREEN_NONE) {
            view.showTvInfoText(getString(R.string.new_version_available), true);
        }
    }

    public void onUpdateVersionClick() {
        updateAppManager.onUpdateVersionClick();
    }
}
