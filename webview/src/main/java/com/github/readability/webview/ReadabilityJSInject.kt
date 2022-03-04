package com.github.readability.webview

import android.content.Context
import android.util.Base64
import android.webkit.WebView
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.atomic.AtomicReference

object ReadabilityJSInject {

    private val js = AtomicReference<String>()

    fun injectReadability(webView: WebView) {
        try {
            webView.loadUrl(
                "javascript:(function() {" +
                    "var scriptElement = document.getElementById('readability-script');" + // "alert(scriptElement);" +
                    "if(!scriptElement) {" +
                    // "var meta = document.createElement('meta');" +
                    // "meta.httpEquiv = \"Content-Security-Policy\";" +
                    // "meta.content = \"default-src 'none'; img-src 'self';script-src 'self'; style-src 'self'\";" +
                    // "document.getElementsByTagName('head')[0].appendChild(meta);"+
                    "var parent = document.body;" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    "script.id = 'readability-script';" + // Tell the browser to BASE64-decode the string into your script !!!
                    "script.innerHTML = window.atob('" + readabilityJS(webView.context.applicationContext) + "');" +
                    "parent.appendChild(script);}" +
                    "})()"
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun readabilityToggle(webView: WebView) {
        webView.loadUrl("javascript:renderReadabilityToggle();")
    }

    fun decodeReadabilityJS(context: Context): String {
        return String(Base64.decode(readabilityJS(context.applicationContext), Base64.NO_WRAP))
    }

    private fun readabilityJS(context: Context): String {
        if (js.get().isNullOrEmpty()) {
            val text = loadJSFromAssets(context)
            js.set(text)
        }
        return js.get()
    }

    private fun loadJSFromAssets(context: Context): String {
        var input: InputStream? = null
        return try {
            input = context.assets.open("readability")
            val buffer = ByteArray(input.available())
            input.read(buffer)
            input.close()
            Base64.encodeToString(buffer, Base64.NO_WRAP)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            input?.close()
            ""
        }
    }
}
