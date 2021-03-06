package me.yeojoy.hancahouse

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web.*

import me.yeojoy.hancahouse.app.Constants

class WebActivity : AppCompatActivity() {

    companion object {
        val TAG = WebActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent == null) {
            finish()
            return
        }

        var url : String = intent.getStringExtra(Constants.KEY_INTENT_URL) ?: ""

        if (TextUtils.isEmpty(url)) {
            finish()
            return
        }

        if (!url.startsWith("https://")) {
            url = Constants.HOST + url
        }

        Log.d(TAG, "Url >>> $url")

        setupWebViewSettings(webview)

        webview.loadUrl(url)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupWebViewSettings(webView : WebView){
        val settings : WebSettings = webView.settings
//        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.loadsImagesAutomatically = true
        settings.useWideViewPort = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.userAgentString = "app"
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = HancaWebViewClient(progressBarLoading)
        settings.setSupportZoom(false)
        settings.setAppCacheEnabled(false)
    }

    private class HancaWebViewClient(private val progressBar: ProgressBar) : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            progressBar.visibility = View.GONE
        }
    }
}
