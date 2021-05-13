package com.triskelapps.alcalasuena.ui.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.databinding.ActivitySplashBinding;
import com.triskelapps.alcalasuena.views.UpdateAppView;

/**
 * Created by julio on 29/05/17.
 */

public class SplashActivity extends BaseActivity implements SplashView, View.OnClickListener {
    private SplashPresenter presenter;
    private ActivitySplashBinding binding;

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = SplashPresenter.newInstance(this, this);
        binding = ActivitySplashBinding.inflate(LayoutInflater.from(this));
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding.imgAlcalasuenaSplash.setOnClickListener(this);
        binding.tvSplashInfo.setOnClickListener(this);

        binding.viewUpdateApp.setCustomListener(() -> presenter.onUpdateAvailable(), false);

        presenter.onCreate(getIntent());

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_splash_info:
                presenter.onSplashInfoTextClick();
                break;

            case R.id.img_alcalasuena_splash:
                presenter.onSplashImageClick();
                break;
        }
    }

    @Override
    public void showTvInfoText(String text) {
        binding.tvSplashInfo.setText(text);
    }

}
