package com.github.readability.samples

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.github.readability.samples.unsafe.HttpEngine
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private val webView by lazy { findViewById<WebView>(R.id.webView) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progressBar) }

    private val webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView, url: String?) {
            super.onPageFinished(view, url)
            progressBar.visibility = View.INVISIBLE
            // ReadabilityJSInject.injectReadability(view)
        }

        override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
            Log.d("WebView", url)
            val resourceResponse = HttpEngine.handleRequestViaOkHttp(url)
            if (resourceResponse != null) {
                return resourceResponse
            }
            return super.shouldInterceptRequest(view, url)
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
            Log.d("WebView", request?.url?.toString() ?: "")
            val resourceResponse = HttpEngine.handleRequestViaOkHttp(request)
            if (resourceResponse != null) {
                return resourceResponse
            }
            return super.shouldInterceptRequest(view, request)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (processUrl(view, url)) {
                return true
            }
            return super.shouldOverrideUrlLoading(view, url)
        }

        fun processUrl(view: WebView?, url: String): Boolean {
            // 拦截url点击
            if (!url.startsWith("http")) {
                return true
            }
            return url.contains("download")
        }
    }

    private val webViewChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            progressBar.progress = newProgress
            if (newProgress > 80) {
                // ReadabilityJSInject.injectReadability(view)
            }
        }

        override fun onReceivedTitle(view: WebView, title: String?) {
            super.onReceivedTitle(view, title)
            // ReadabilityJSInject.injectReadability(view)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WebInit.init(webView)

        webView.webChromeClient = webViewChromeClient
        webView.webViewClient = webViewClient

        findViewById<View>(R.id.loadURLs).setOnClickListener {
            SampleURLs.show(this@MainActivity) { url ->
                HttpEngine.prepareSet.add(url)
                // webView.loadUrl(url)

                webView.loadUrl(
                    "file:///android_asset/readerview/readerview.html?ref=${
                    URLEncoder.encode(
                        url,
                        "UTF-8"
                    )
                    }"
                )
            }
        }

        findViewById<View>(R.id.readability).setOnClickListener {
            // ReadabilityJSInject.readabilityToggle(webView)
        }

        webView.loadUrl(
            "file:///android_asset/readerview/readerview.html?ref=${
            URLEncoder.encode(
                "https://www.zhihu.com/question/47819047/answer/108130984",
                "UTF-8"
            )
            }"
        )
    }

    override fun onResume() {
        super.onResume()
        webView.resumeTimers()
    }

    override fun onPause() {
        super.onPause()
        webView.pauseTimers()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
            return
        }
        super.onBackPressed()
    }
}
