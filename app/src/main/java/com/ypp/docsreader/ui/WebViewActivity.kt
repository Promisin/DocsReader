package com.ypp.docsreader.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import com.ypp.docsreader.R
import com.ypp.docsreader.model.cmd.JSCmdFactory
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val targetUrl = intent.getStringExtra("targetUrl")
        val type = intent.getStringExtra("type")
        val cmd = JSCmdFactory.getCmdByType(type ?: "empty")
        val loadingProgressBar: ContentLoadingProgressBar = findViewById(R.id.loading_pb)
        val webView: WebView = findViewById(R.id.webView)
        webView.loadUrl(targetUrl)
        webView.settings.javaScriptEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.setSupportZoom(true)
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                webView.loadUrl(cmd.hideOthers)
                webView.loadUrl(cmd.executeHide)
                loadingProgressBar.hide()
                Thread.sleep(500)
                webView.visibility = View.VISIBLE
            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
            return
        }
        super.onBackPressed()
    }
}