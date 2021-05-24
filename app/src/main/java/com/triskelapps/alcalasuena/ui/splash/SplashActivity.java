package com.triskelapps.alcalasuena.ui.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.databinding.ActivitySplashBinding;
import com.triskelapps.alcalasuena.util.update_app.UpdateAppManager;

/**
 * Created by julio on 29/05/17.
 */

public class SplashActivity extends BaseActivity implements SplashView {
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

        binding.imgAlcalasuenaSplash.setOnClickListener(v -> presenter.onSplashImageClick());

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
    public void showTvInfoText(String text, boolean enableClick) {
        binding.tvSplashInfo.setText(text);
        binding.tvSplashInfo.setOnClickListener(enableClick ? v -> presenter.onUpdateVersionClick() : null);
    }

}
