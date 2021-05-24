package com.triskelapps.alcalasuena.util.update_app;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.triskelapps.alcalasuena.BuildConfig;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.databinding.ViewUpdateAppBinding;


public class UpdateAppView extends FrameLayout implements LifecycleObserver {

    private static final String TAG = UpdateAppView.class.getSimpleName();

    private UpdateAppManager updateAppManager;
    private ViewUpdateAppBinding binding;

    public UpdateAppView(Context context) {
        super(context);
        init();
    }


    public UpdateAppView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UpdateAppView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        binding = ViewUpdateAppBinding.inflate(LayoutInflater.from(getContext()), this, true);

        binding.btnUpdateApp.setOnClickListener(v -> updateAppManager.onUpdateVersionClick());
        binding.btnCloseUpdateAppView.setOnClickListener(v -> setVisibility(GONE));

        setVisibility(GONE);

        configure();
    }

    private void configure() {

        updateAppManager = new UpdateAppManager(getContext());
        updateAppManager.setUpdateAvailableListener(() -> setVisibility(VISIBLE));

        if (getContext() instanceof Activity) {
            checkUpdateAvailable();
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, "onAttachedToWindow: ");
        if (getContext() instanceof AppCompatActivity) {
            ((AppCompatActivity) getContext()).getLifecycle().addObserver(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.i(TAG, "onDetachedFromWindow: ");
        super.onDetachedFromWindow();
        if (getContext() instanceof AppCompatActivity) {
            ((AppCompatActivity) getContext()).getLifecycle().removeObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void checkUpdateInProgress() {
        Log.i(TAG, "checkUpdateInProgress: ");

        if (BuildConfig.DEBUG) {
            return;
        }

        updateAppManager.onResume();

    }

    private void checkUpdateAvailable() {

        if (BuildConfig.DEBUG) {
            return;
        }

        updateAppManager.checkUpdateAvailable();

    }

}
