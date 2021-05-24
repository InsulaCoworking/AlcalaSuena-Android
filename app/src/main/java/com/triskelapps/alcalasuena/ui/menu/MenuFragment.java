package com.triskelapps.alcalasuena.ui.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseFragment;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.ui.about.AboutInsulaActivity;
import com.triskelapps.alcalasuena.ui.bands.BandsPresenter;
import com.triskelapps.alcalasuena.ui.map.MapActivity;
import com.triskelapps.alcalasuena.ui.news.NewsActivity;
import com.triskelapps.alcalasuena.ui.news.send.SendNewsActivity;
import com.triskelapps.alcalasuena.ui.splash.SplashPresenter;
import com.triskelapps.alcalasuena.ui.venues.VenuesPresenter;
import com.triskelapps.alcalasuena.util.Util;
import com.triskelapps.alcalasuena.util.WebUtils;

/**
 * Created by julio on 23/05/17.
 */

public class MenuFragment extends BaseFragment implements View.OnClickListener, View.OnLongClickListener {
    private View btnMenuBands;
    private View btnMenuMap;
    private View btnMenuVenues;
    private View btnMenuAbout;
    private View btnLogoAytoAlcala;
    private View btnLogoAlcalaSuena;
    private View btnLogoAlcalaEsMusica;
    private View btnMenuAboutInsula;
    private View btnMenuNews;

    private void findViews(View layout) {
        btnMenuBands = layout.findViewById( R.id.btn_menu_bands );
        btnMenuMap = layout.findViewById( R.id.btn_menu_map );
        btnMenuVenues = layout.findViewById( R.id.btn_menu_venues );
        btnMenuAbout = layout.findViewById( R.id.btn_menu_about );
        btnMenuNews = layout.findViewById( R.id.btn_menu_news);
        btnMenuAboutInsula = layout.findViewById( R.id.btn_about_insula );

        btnLogoAlcalaSuena = layout.findViewById(R.id.btn_logo_alcalasuena);
        btnLogoAlcalaEsMusica = layout.findViewById(R.id.btn_logo_alcalaesmusica);
        btnLogoAytoAlcala = layout.findViewById(R.id.btn_logo_ayto_alcala);

        btnMenuBands.setOnClickListener(this);
        btnMenuMap.setOnClickListener(this);
        btnMenuVenues.setOnClickListener(this);
        btnMenuAbout.setOnClickListener(this);
        btnMenuNews.setOnClickListener(this);
        btnMenuAboutInsula.setOnClickListener(this);

        btnLogoAlcalaSuena.setOnClickListener(this);
        btnLogoAlcalaEsMusica.setOnClickListener(this);
        btnLogoAytoAlcala.setOnClickListener(this);

        btnLogoAlcalaEsMusica.setOnLongClickListener(this);
    }


    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_menu, container, false);
        findViews(layout);

        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu_bands:
                startActivity(BandsPresenter.newBandsActivity(getActivity()));
                break;

            case R.id.btn_menu_about:
                SplashPresenter.launchSplashActivity(getActivity(), SplashPresenter.NEXT_SCREEN_ABOUT);
//                startActivity(new Intent(getActivity(), AboutAlcalaSuenaActivity.class));
                break;

            case R.id.btn_menu_map:
                startActivity(new Intent(getActivity(), MapActivity.class));
                break;

            case R.id.btn_menu_venues:
                startActivity(VenuesPresenter.newVenuesActivity(getActivity()));
                break;

            case R.id.btn_menu_news:
                startActivity(new Intent(getActivity(), NewsActivity.class));
                break;

            case R.id.btn_about_insula:
                startActivity(new Intent(getActivity(), AboutInsulaActivity.class));
//                RealmBrowser.startRealmModelsActivity(getActivity(), Realm.getDefaultConfiguration());
                break;


            case R.id.btn_logo_alcalasuena:
            case R.id.btn_logo_alcalaesmusica:
            case R.id.btn_logo_ayto_alcala:
                String url = (String) v.getTag();
                WebUtils.openCustomTab(getActivity(), url);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        showPinSendNewsDialog();
        return false;
    }

    private void showPinSendNewsDialog() {
        View view = View.inflate(getActivity(), R.layout.view_pin_send_news, null);
        final EditText editText = view.findViewById(R.id.edit_pin);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.secret_key)
                .setView(view)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pin = getString(R.string.pin_send_news);
                        String userPin = editText.getText().toString();
                        if (TextUtils.equals(pin, userPin)) {
                            String deviceId = Util.getDeviceId(getActivity());
                            getPrefs().edit().putString(App.SHARED_PIN_SEND_NEWS_ENCRIPT, Util.getMD5Hash(pin + deviceId)).commit();
                            startActivity(new Intent(getActivity(), SendNewsActivity.class));
                        }
                    }
                })
                .setNeutralButton(R.string.back, null)
                .show();
    }

}
