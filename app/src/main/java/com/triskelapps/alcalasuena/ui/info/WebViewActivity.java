package com.triskelapps.alcalasuena.ui.info;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;



public class WebViewActivity extends BaseActivity {

    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_FILENAME = "extra_filename";
    private static final String EXTRA_TITLE = "extra_title";


    public static final String FILENAME_QUE_ES_MES = "que_es_mes.html";
    public static final String FILENAME_COMO_FUNCIONA_BONIATO = "como_funciona_boniato.html";

    private WebView webView;
    private ProgressBar progressWebview;
    private String url;


    public static Intent getRemoteUrlIntent(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    public static void startRemoteUrl(Context context, String title, String url) {
        context.startActivity(getRemoteUrlIntent(context, title, url));
    }

    public static void startLocalHtml(Context context, String title, String filename) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_FILENAME, filename);
        context.startActivity(intent);
    }

    private void findViews() {
        webView = (WebView) findViewById(R.id.webview);
        progressWebview = (ProgressBar) findViewById(R.id.progress_webview);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);

        findViews();

        configureSecondLevelActivity();
        configWebView();

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (title != null) {
            setToolbarTitle(title);
        }

        if (getIntent().hasExtra(EXTRA_URL)) {
            url = getIntent().getStringExtra(EXTRA_URL);
//            webView.loadUrl(url);

        } else if (getIntent().hasExtra(EXTRA_FILENAME)) {
            String filename = getIntent().getStringExtra(EXTRA_FILENAME);
            loadHtml(filename);

        } else {
            throw new IllegalArgumentException(
                    "Not passed url or filename extra parameter");
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(0);

        sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        finish();

    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return super.onCreateOptionsMenu(menu);
    }



    private void loadHtml(String htmlFile) {

        webView.loadUrl("file:///android_asset/html/" + htmlFile);

    }

    private void configWebView() {

//        webView.setBackgroundColor(Color.TRANSPARENT);

        WebSettings webviewSettings = webView.getSettings();

        // webviewSettings.setLoadWithOverviewMode(true);
        webviewSettings.setJavaScriptEnabled(true);
        webviewSettings.setPluginState(PluginState.ON);

        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menuItem_web:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressWebview.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressWebview.setVisibility(View.GONE);
            progressWebview.setProgress(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressWebview.setVisibility(View.VISIBLE);
            progressWebview.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }
    }

}
