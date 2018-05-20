package com.triskelapps.alcalasuena.ui.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.ui.about.AboutAlcalaSuenaActivity;
import com.triskelapps.alcalasuena.ui.intro.IntroPresenter;
import com.triskelapps.alcalasuena.util.Util;

/**
 * Created by julio on 29/05/17.
 */


public class SplashPresenter extends BasePresenter {

    public static final int NEXT_SCREEN_INTRO = 0;
    public static final int NEXT_SCREEN_ABOUT = 1;

    private int nextScreen;

    private final BaseView view;
    private Handler handler;
    private boolean launchMainActivity = true;

    public static void launchSplashActivity(Context context, int nextScreen) {

        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra(Util.EXTRA_INT, nextScreen);
        context.startActivity(intent);
    }

    public static SplashPresenter newInstance(BaseView view, Context context) {

        return new SplashPresenter(view, context);

    }

    private SplashPresenter(BaseView view, Context context) {
        super(context, view);

        this.view = view;

    }


    private Runnable runnableNextScreen = new Runnable() {
        @Override
        public void run() {
            // If connection failed, launch it anyway
            launchNextScreen();
        }
    };

    public void onCreate(Intent intent) {

        if (intent.hasExtra(Util.EXTRA_INT)) {
            nextScreen = intent.getIntExtra(Util.EXTRA_INT, -1);
        } else {
            throw new IllegalArgumentException("Next screen parameter needed");
        }

        handler = new Handler();

//        if (intent.getAction() != null && intent.getAction().equals(ACTION_SHOW_NOTIFICATION)) {
//
//            launchMainActivity = false;
//            String title = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE);
//            String message = intent.getStringExtra(EXTRA_NOTIFICATION_MESSAGE);
//            String btnText = intent.getStringExtra(EXTRA_NOTIFICATION_CUSTOM_BUTTON_TEXT);
//            String btnLink = intent.getStringExtra(EXTRA_NOTIFICATION_CUSTOM_BUTTON_LINK);
//
//            DialogShowNotification.newInstace(context).show(title, message, btnText, btnLink);
//        }
    }


    public void onResume() {

        handler.postDelayed(runnableNextScreen, 3000);
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
        }

        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(0, 0);
    }

}
