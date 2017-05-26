package com.triskelapps.alcalasuena.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;

/**
 * Created by julio on 26/05/17.
 */

public class AboutActivity extends BaseActivity {

    private static final String URL_DOSSIER = "https://www.canva.com/design/DACUpX9urHM/0fEzcfON3oPVNcIgHZTKpQ/view?website";

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        configureSecondLevelActivity();
        
        findViewById(R.id.btn_see_dossier).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_DOSSIER)));
            }
        });
    }
}
