package com.github.readability.webview

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.github.webview.resources.ReaderJS
import org.json.JSONObject
import java.net.URLEncoder

object ReaderJSInterface : ReaderJS {

    private const val TAG = "ReaderJSInterface"

    fun exeJavaScript(webView: WebView, action: JSONObject) {
        webView.evaluateJavascript("updateTheme($action)", null)
    }

    fun renderReadabilityPage(webView: WebView, url: String) {
        webView.loadUrl(
            "file:///android_asset/readerview/readerview.html?ref=${
            URLEncoder.encode(
                url,
                "UTF-8"
            )
            }"
        )
    }

    @JavascriptInterface
    fun readerResult(data: String) {
        Log.d(TAG, "$data")
        ReaderJS.theme = JSONObject(data)
    }
}
