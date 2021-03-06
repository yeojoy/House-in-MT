package me.yeojoy.hancahouse;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.yeojoy.hancahouse.app.Constants;

public class WebActivity extends AppCompatActivity implements Constants {

    private static final String TAG = WebActivity.class.getSimpleName();

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        String url = intent.getStringExtra(KEY_INTENT_URL);

        if (TextUtils.isEmpty(url)) {
            finish();
            return;
        }

        if (!url.startsWith("https://")) {
            url = HOST + url;
        }

        Log.d(TAG, "Url >>> " + url);

        WebView webView = findViewById(R.id.webview);
        settingWebview(webView);
        mProgressBar = findViewById(R.id.progress_bar_loading);

        webView.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void settingWebview(WebView webView){
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setUserAgentString("app");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new HancaWebViewClient());
    }

    private class HancaWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            mProgressBar.setVisibility(View.GONE);
        }
    }
}
