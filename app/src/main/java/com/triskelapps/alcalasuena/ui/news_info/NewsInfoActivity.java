package com.triskelapps.alcalasuena.ui.news_info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.News;
import com.triskelapps.alcalasuena.ui.image_full.ImageFullActivity;
import com.triskelapps.alcalasuena.ui.info.WebViewActivity;
import com.triskelapps.alcalasuena.util.DateUtils;

public class NewsInfoActivity extends BaseActivity implements NewsInfoView {

    private NewsInfoPresenter presenter;
    private TextView tvNewsTitle;
    private ImageView imgNews;
    private TextView tvNewsText;
    private Button btnNewsLink;
    private TextView tvNewsDate;

    private void findViews() {
        tvNewsTitle = (TextView) findViewById(R.id.tv_news_title);
        imgNews = (ImageView) findViewById(R.id.img_news);
        tvNewsText = (TextView) findViewById(R.id.tv_news_text);
        btnNewsLink = (Button) findViewById(R.id.btn_news_link);
        tvNewsDate = findViewById(R.id.tv_news_date);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = NewsInfoPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);

        findViews();
        configureSecondLevelActivity();

        presenter.onCreate(getIntent());
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }


    @Override
    public void showNews(final News news) {
        tvNewsTitle.setText(news.getTitle());
        tvNewsText.setText(news.getText());
        tvNewsDate.setText(DateUtils.convertDateTimeApiToUserFormat(news.getStart_date()));

        if (news.getImage() != null) {

            Picasso.get()
                    .load(news.getImageUrlFull())
                    .into(imgNews);

            imgNews.setOnClickListener(v -> lauchImageZoom(news.getImageUrlFull()));
        }

        if (news.hasValidLinkButton()) {
            btnNewsLink.setText(news.getBtn_text());
            btnNewsLink.setOnClickListener(v ->
                    WebViewActivity.startRemoteUrl(NewsInfoActivity.this, null, news.getBtn_link()));
        } else {
            btnNewsLink.setVisibility(View.GONE);
        }

        if (news.getNative_code() != null) {
            switch (news.getNative_code()) {
                case 1:

                    btnNewsLink.setVisibility(View.VISIBLE);
                    btnNewsLink.setText(R.string.share_app);
                    btnNewsLink.setOnClickListener(v -> {

                        String text = String.format(getString(R.string.share_app_text), App.URL_GOOGLE_PLAY_APP, App.URL_APPLE_STORE_APP);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, text);
                        intent.setType("text/plain");
                        startActivity(intent);
                    });

                    break;
            }
        }
    }

    private void lauchImageZoom(Uri imageUrlFull) {

        startActivity(ImageFullActivity.newImageFullActivity(this, imageUrlFull.toString()));
    }
}
