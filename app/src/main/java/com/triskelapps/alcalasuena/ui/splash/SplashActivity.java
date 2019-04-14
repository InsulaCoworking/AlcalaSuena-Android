package com.triskelapps.alcalasuena.ui.splash;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;

/**
 * Created by julio on 29/05/17.
 */

public class SplashActivity extends BaseActivity implements SplashView, View.OnClickListener {
    private SplashPresenter presenter;
    private TextView tvSplashInfo;

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = SplashPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvSplashInfo = (TextView) findViewById(R.id.tv_splash_info);
        tvSplashInfo.setOnClickListener(this);

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
        }
    }

    @Override
    public void showTvInfoText(String text) {
        tvSplashInfo.setText(text);
    }

}
