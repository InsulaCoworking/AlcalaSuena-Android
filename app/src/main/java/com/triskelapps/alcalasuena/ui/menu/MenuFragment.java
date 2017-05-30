package com.triskelapps.alcalasuena.ui.menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseFragment;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.ui.about.AboutAlcalaSuenaActivity;
import com.triskelapps.alcalasuena.ui.about.AboutInsulaActivity;
import com.triskelapps.alcalasuena.ui.bands.BandsPresenter;
import com.triskelapps.alcalasuena.ui.map.MapActivity;
import com.triskelapps.alcalasuena.ui.venues.VenuesActivity;

/**
 * Created by julio on 23/05/17.
 */

public class MenuFragment extends BaseFragment implements View.OnClickListener {
    private View btnMenuBands;
    private View btnMenuMap;
    private View btnMenuVenues;
    private View btnMenuAbout;
    private View btnLogoAytoAlcala;
    private View btnLogoAlcalaSuena;
    private View btnLogoAlcalaEsMusica;
    private View btnLogoMahou;
    private View btnMenuAboutInsula;

    private void findViews(View layout) {
        btnMenuBands = layout.findViewById( R.id.btn_menu_bands );
        btnMenuMap = layout.findViewById( R.id.btn_menu_map );
        btnMenuVenues = layout.findViewById( R.id.btn_menu_venues );
        btnMenuAbout = layout.findViewById( R.id.btn_menu_about );
        btnMenuAboutInsula = layout.findViewById( R.id.btn_about_insula );

        btnLogoMahou = layout.findViewById(R.id.btn_logo_mahou);
        btnLogoAlcalaSuena = layout.findViewById(R.id.btn_logo_alcalasuena);
        btnLogoAlcalaEsMusica = layout.findViewById(R.id.btn_logo_alcalaesmusica);
        btnLogoAytoAlcala = layout.findViewById(R.id.btn_logo_ayto_alcala);

        btnMenuBands.setOnClickListener(this);
        btnMenuMap.setOnClickListener(this);
        btnMenuVenues.setOnClickListener(this);
        btnMenuAbout.setOnClickListener(this);
        btnMenuAboutInsula.setOnClickListener(this);

        btnLogoMahou.setOnClickListener(this);
        btnLogoAlcalaSuena.setOnClickListener(this);
        btnLogoAlcalaEsMusica.setOnClickListener(this);
        btnLogoAytoAlcala.setOnClickListener(this);
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
                startActivity(new Intent(getActivity(), AboutAlcalaSuenaActivity.class));
                break;

            case R.id.btn_menu_map:
                startActivity(new Intent(getActivity(), MapActivity.class));
                break;

            case R.id.btn_menu_venues:
                startActivity(new Intent(getActivity(), VenuesActivity.class));
                break;

            case R.id.btn_about_insula:
                startActivity(new Intent(getActivity(), AboutInsulaActivity.class));
                break;


            case R.id.btn_logo_mahou:
            case R.id.btn_logo_alcalasuena:
            case R.id.btn_logo_alcalaesmusica:
            case R.id.btn_logo_ayto_alcala:
                String url = (String) v.getTag();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
        }
    }
}
