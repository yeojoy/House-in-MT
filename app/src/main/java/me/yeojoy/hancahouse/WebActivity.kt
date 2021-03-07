package me.yeojoy.hancahouse

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toolbar

import androidx.appcompat.app.AppCompatActivity

import me.yeojoy.hancahouse.app.Constants

class WebActivity : AppCompatActivity() {

    companion object {
        val TAG = WebActivity::class.simpleName
    }

    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val toolbar : Toolbar = findViewById(R.id.toolbar)

        setActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent == null) {
            finish()
            return
        }

        var url : String? = intent.getStringExtra(Constants.KEY_INTENT_URL)

        if (TextUtils.isEmpty(url)) {
            finish()
            return
        }

        if (!url!!.startsWith("https://")) {
            url = Constants.HOST + url
        }

        Log.d(TAG, "Url >>> " + url)

        val webView : WebView = findViewById(R.id.webview)
        progressBar = findViewById(R.id.progress_bar_loading)
        settingWebview(webView)

        webView.loadUrl(url)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun settingWebview(webView : WebView){
        val settings : WebSettings = webView.getSettings()
        settings.setJavaScriptEnabled(true)
        settings.setJavaScriptCanOpenWindowsAutomatically(true)
        settings.setLoadsImagesAutomatically(true)
        settings.setUseWideViewPort(true)
        settings.setSupportZoom(false)
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE)
        settings.setAppCacheEnabled(false)
        settings.setDomStorageEnabled(true)
        settings.setAllowFileAccess(true)
        settings.setUserAgentString("app")
        webView.setWebChromeClient(WebChromeClient())
        webView.setWebViewClient(HancaWebViewClient(progressBar))
    }

    private class HancaWebViewClient(progressBar: ProgressBar) : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

//            progressBar.setVisibility(View.GONE)
        }
    }
}
