package com.github.readability.webview

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import org.json.JSONObject
import java.net.URLEncoder

object ReaderJSInterface {

    private const val TAG = "ReaderJSInterface"

    const val Bridge = "readability"

    const val ACTION_MESSAGE_KEY = "action"
    const val ACTION_SET_COLOR_SCHEME = "setColorScheme"
    const val ACTION_VALUE_COLOR_SCHEME = "colorScheme"

    const val ACTION_CHANGE_FONT_SIZE = "changeFontSize"
    const val ACTION_VALUE_FONT_SIZE = "fontSize"

    const val ACTION_SET_FONT_TYPE = "setFontType"
    const val ACTION_VALUE_FONT_TYPE = "fontType"

    var theme: JSONObject = JSONObject()

    fun exeJavaScript(webView: WebView, action: JSONObject) {
        webView.loadUrl(
            "javascript:(function f() {" +
                "updateTheme($action)" +
                " } )()"
        )
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
        theme = JSONObject(data)
    }
}
