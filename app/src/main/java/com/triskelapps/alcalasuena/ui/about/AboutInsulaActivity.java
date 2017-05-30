package com.triskelapps.alcalasuena.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;

/**
 * Created by julio on 26/05/17.
 */

public class AboutInsulaActivity extends BaseActivity {

    private static final String URL_DOSSIER = "https://www.canva.com/design/DACUpX9urHM/0fEzcfON3oPVNcIgHZTKpQ/view?website";

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_insula);

        configureSecondLevelActivity();
        setToolbarTitle(R.string.about_app);

        iterateLinkableViews((ViewGroup) getWindow().getDecorView().getRootView());
    }

    private void iterateLinkableViews(ViewGroup parent) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                iterateLinkableViews((ViewGroup) child);
                // DO SOMETHING WITH VIEWGROUP, AFTER CHILDREN HAS BEEN LOOPED
            } else {
                if (child != null) {
                    // DO SOMETHING WITH VIEW
                    if (child.getTag() != null) {
                        final String url = (String) child.getTag();
                        child.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                            }
                        });
                    }
                }
            }
        }

    }
}
