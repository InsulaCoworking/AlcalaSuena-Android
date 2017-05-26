package com.triskelapps.alcalasuena.ui.intro;

import android.os.Bundle;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;

public class IntroActivity extends BaseActivity implements IntroView
{

    private IntroPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = IntroPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        presenter.onCreate();
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }
}
