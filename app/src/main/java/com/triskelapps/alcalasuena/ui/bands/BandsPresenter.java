package com.triskelapps.alcalasuena.ui.bands;

import android.content.Context;
import android.content.Intent;

import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.BandInteractor;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.ui.band_info.BandInfoPresenter;

import java.util.List;

/**
 * Created by julio on 23/05/17.
 */


 public class BandsPresenter extends BasePresenter {

     private final BandsView view;
    private final BandInteractor bandInteractor;

    public static Intent newBandsActivity(Context context) {

         Intent intent = new Intent(context, BandsActivity.class);

         return intent;
     }

     public static BandsPresenter newInstance(BandsView view, Context context) {

         return new BandsPresenter(view, context);

     }

     private BandsPresenter(BandsView view, Context context) {
         super(context, view);

         this.view = view;
         bandInteractor = new BandInteractor(context, view);

     }

     public void onCreate() {

         refreshData();
     }


    public void onResume() {

     }

     public void refreshData() {

         List<Band> bands = bandInteractor.getBandsDB();
         view.showBands(bands);

     }


    public void onSearchTextChanged(String text) {

        List<Band> bands = bandInteractor.getBandsDB(text);
        view.showBands(bands);
    }

    public void onBandClicked(int idBand) {
        context.startActivity(BandInfoPresenter.newBandInfoActivity(context, idBand));
    }
}
