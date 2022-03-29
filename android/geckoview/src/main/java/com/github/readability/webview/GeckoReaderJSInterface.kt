package com.github.readability.webview

import android.util.Log
import android.webkit.JavascriptInterface
import com.github.webview.resources.ReaderJS
import org.json.JSONObject
import org.mozilla.geckoview.GeckoSession
import java.net.URLEncoder

object GeckoReaderJSInterface : ReaderJS {

    private const val TAG = "GeckoReaderJSInterface"

    fun exeJavaScript(geckoSession: GeckoSession, action: JSONObject) {
        geckoSession.loadUri(
            "javascript:(function f() {" +
                "updateTheme($action)" +
                " } )()"
        )
    }

    fun renderReadabilityPage(geckoSession: GeckoSession, url: String) {
        val target = "resource://android/assets/readerview/readerview.html?ref=${
        URLEncoder.encode(
            url,
            "UTF-8"
        )
        }"
        geckoSession.loadUri(target)
    }

    @JavascriptInterface
    fun readerResult(data: String) {
        Log.d(TAG, "$data")
        ReaderJS.theme = JSONObject(data)
    }
}
