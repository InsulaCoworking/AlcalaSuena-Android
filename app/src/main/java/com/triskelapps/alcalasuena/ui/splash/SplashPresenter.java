package com.triskelapps.alcalasuena.ui.splash;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.ui.MainPresenter;
import com.triskelapps.alcalasuena.ui.intro.IntroPresenter;

import static com.triskelapps.alcalasuena.App.EXTRA_FIRST_TIME_APP_LAUNCHING;

/**
 * Created by julio on 29/05/17.
 */


 public class SplashPresenter extends BasePresenter {

     private final BaseView view;
    private Handler handler;

    public static Intent newSplashActivity(Context context) {

         Intent intent = new Intent(context, SplashActivity.class);

         return intent;
     }

     public static SplashPresenter newInstance(BaseView view, Context context) {

         return new SplashPresenter(view, context);

     }

     private SplashPresenter(BaseView view, Context context) {
         super(context, view);

         this.view = view;

     }

    private BroadcastReceiver receiverRefreshData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(App.ACTION_REFRESH_DATA)) {

                // update data from api finished
                launchNextScreen();
            }
        }
    };

    private Runnable runnableNextScreen = new Runnable() {
        @Override
        public void run() {
            // If connection failed, launch it anyway
            launchNextScreen();
        }
    };

     public void onCreate() {

         handler = new Handler();
     }

     public void onResume() {

         IntentFilter intentFilter = new IntentFilter();
         intentFilter.addAction(App.ACTION_REFRESH_DATA);
         context.registerReceiver(receiverRefreshData, intentFilter);

         handler.postDelayed(runnableNextScreen, 5000);
     }

     public void onPause() {
         handler.removeCallbacks(runnableNextScreen);
         context.unregisterReceiver(receiverRefreshData);
     }


     private void launchNextScreen() {

         if (getPrefs().getBoolean(EXTRA_FIRST_TIME_APP_LAUNCHING, true)) {
             launchIntroActivity();
         } else {
             launchMainActivity();
         }
     }

    private void launchIntroActivity() {
        context.startActivity(IntroPresenter.newIntroActivity(context));
        ((Activity)context).finish();
        ((Activity)context).overridePendingTransition(0,0);
    }

    private void launchMainActivity() {
        context.startActivity(MainPresenter.newMainActivity(context));
        ((Activity)context).finish();
        ((Activity)context).overridePendingTransition(0,0);
    }

 }
