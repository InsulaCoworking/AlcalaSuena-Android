package com.triskelapps.alcalasuena.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import com.triskelapps.alcalasuena.DebugHelper;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;

/**
 * Created by julio on 29/05/17.
 */

public class SplashActivity extends BaseActivity {
    private SplashPresenter presenter;

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = SplashPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (DebugHelper.SHORTCUT_ACTIVITY != null) {
            startActivity(new Intent(this, DebugHelper.SHORTCUT_ACTIVITY));
        }

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
}
