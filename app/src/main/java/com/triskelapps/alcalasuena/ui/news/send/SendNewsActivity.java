package com.triskelapps.alcalasuena.ui.news.send;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.util.WindowUtils;

public class SendNewsActivity extends BaseActivity implements View.OnClickListener, SendNewsView {

    private AppCompatEditText editSendNewsTitle;
    private AppCompatEditText editSendNewsText;
    private AppCompatButton btnCamera;
    private AppCompatButton btnGallery;
    private AppCompatImageView imgSendNews;
    private SendNewsPresenter presenter;
    private AppCompatEditText editLink;
    private AppCompatEditText editLinkButtonText;

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    private void findViews() {
        editSendNewsTitle = (AppCompatEditText)findViewById( R.id.edit_send_news_title);
        editSendNewsText = (AppCompatEditText)findViewById( R.id.edit_send_news_text);
        editLink = (AppCompatEditText)findViewById( R.id.edit_link);
        editLinkButtonText = (AppCompatEditText)findViewById( R.id.edit_link_button_text);
        btnCamera = (AppCompatButton)findViewById( R.id.btn_camera );
        btnGallery = (AppCompatButton)findViewById( R.id.btn_gallery );
        imgSendNews = (AppCompatImageView)findViewById( R.id.img_send_news );

        btnCamera.setOnClickListener( this );
        btnGallery.setOnClickListener( this );
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = SendNewsPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_news);
        configureSecondLevelActivity();
        findViews();

        presenter.onCreate();

        editSendNewsTitle.requestFocus();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send_news, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItem_send_news:
                String title = editSendNewsTitle.getText().toString();
                String text = editSendNewsText.getText().toString();
                String link = editLink.getText().toString();
                String linkButtonText = editLinkButtonText.getText().toString();
                presenter.onSendButtonClick(title, text, link, linkButtonText);

                WindowUtils.hideSoftKeyboard(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        WindowUtils.hideSoftKeyboard(this);

        switch (v.getId()) {
            case R.id.btn_camera:
                presenter.onCameraButtonClick();
                break;

            case R.id.btn_gallery:
                presenter.onGalleryButtonClick();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void setImageUri(Uri uri) {
        Picasso.with(this)
                .load(uri)
                .into(imgSendNews);
    }
}
