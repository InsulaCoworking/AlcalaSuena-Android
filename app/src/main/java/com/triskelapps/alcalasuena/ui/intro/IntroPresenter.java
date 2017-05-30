package com.triskelapps.alcalasuena.ui.intro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.BandInteractor;
import com.triskelapps.alcalasuena.interactor.VenueInteractor;
import com.triskelapps.alcalasuena.ui.MainPresenter;

import static com.triskelapps.alcalasuena.App.EXTRA_FIRST_TIME_APP_LAUNCHING;

/**
 * Created by julio on 26/05/17.
 */


 public class IntroPresenter extends BasePresenter {

     private final IntroView view;
    private final BandInteractor bandInteractor;
    private final VenueInteractor venueInteractor;

    public static Intent newIntroActivity(Context context) {

         Intent intent = new Intent(context, IntroActivity.class);

         return intent;
     }

     public static IntroPresenter newInstance(IntroView view, Context context) {

         return new IntroPresenter(view, context);

     }

     private IntroPresenter(IntroView view, Context context) {
         super(context, view);

         this.view = view;
         bandInteractor = new BandInteractor(context, view);
         venueInteractor = new VenueInteractor(context, view);

     }

    public void onCreate() {

        if (!getPrefs().getBoolean(EXTRA_FIRST_TIME_APP_LAUNCHING, true)) {
            lauchMainActivity();
        }
    }

    private void lauchMainActivity() {
        context.startActivity(MainPresenter.newMainActivity(context));
        ((Activity)context).finish();
        ((Activity)context).overridePendingTransition(0,0);
    }


    public void onFinishIntro() {
        lauchMainActivity();
    }
}
