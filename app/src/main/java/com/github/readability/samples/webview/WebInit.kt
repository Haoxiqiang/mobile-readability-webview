package com.github.readability.samples.webview

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.webkit.WebSettings
import android.webkit.WebView

object WebInit {

    @Suppress("DEPRECATION")
    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt", "Deprecated")
    fun init(webView: WebView) {

        val webSettings = webView.settings

        webSettings.loadsImagesAutomatically = true
        webSettings.databaseEnabled = true
        webSettings.allowContentAccess = true
        webSettings.domStorageEnabled = true
        webSettings.allowFileAccess = true

        webSettings.cacheMode = WebSettings.LOAD_DEFAULT

        webSettings.defaultTextEncodingName = "UTF-8"
        webSettings.javaScriptEnabled = true

        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                webView.removeJavascriptInterface("searchBoxJavaBridge_")
                webView.removeJavascriptInterface("accessibility")
                webView.removeJavascriptInterface("accessibilityTraversal")
            }
        } catch (tr: Throwable) {
            tr.printStackTrace()
        }
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.setGeolocationEnabled(true)
        webSettings.blockNetworkImage = false

        webView.setBackgroundColor(Color.argb(1, 0, 0, 0))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.mediaPlaybackRequiresUserGesture = false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        webSettings.allowUniversalAccessFromFileURLs = true
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.setSupportZoom(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
    }
}
