package com.github.readability.samples.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.github.readability.samples.R
import com.github.readability.samples.SampleURLs
import com.github.readability.samples.StyleSheet
import com.github.readability.webview.ReaderJS
import com.github.readability.webview.ReaderJSInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WebViewActivity : AppCompatActivity() {

    private val titleView by lazy { findViewById<TextView>(R.id.title) }
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
        }

        @Suppress("DEPRECATION")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (processUrl(url)) {
                return true
            }
            return super.shouldOverrideUrlLoading(/* view = */ view, /* url = */ url)
        }

        fun processUrl(url: String): Boolean {
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
        }

        override fun onReceivedTitle(view: WebView, title: String?) {
            super.onReceivedTitle(view, title)
            titleView.text = title
            titleView.ellipsize = TextUtils.TruncateAt.MARQUEE
        }
    }

    private fun isWebViewInForceDarkMode(): Boolean {
        return if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            WebSettingsCompat.getForceDark(webView.settings) == WebSettingsCompat.FORCE_DARK_ON
        } else {
            false
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        WebInit.init(webView)

        findViewById<View>(R.id.more).setOnClickListener {
            SampleURLs.show(this@WebViewActivity, urlPicker = { url ->
                webView.clearHistory()
                ReaderJSInterface.renderReadabilityPage(webView, url)
            }, dismiss = {
            })
        }

        findViewById<FloatingActionButton>(R.id.style).setOnClickListener {
            StyleSheet()
                .apply {
                    styleTheme = { st ->
                        // if WebView in force dark mode, theme won't works.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && isWebViewInForceDarkMode()) {
                            Toast.makeText(
                                applicationContext,
                                "webView.settings.forceDark, theme won't works.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ReaderJSInterface.exeJavaScript(webView, st)
                    }
                    styleFontSize = { sf ->
                        ReaderJSInterface.exeJavaScript(webView, sf)
                    }
                }
                .show(supportFragmentManager, "StyleSheet")
        }

        // if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
        //    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        //        Configuration.UI_MODE_NIGHT_YES -> WebSettingsCompat.setForceDark(
        //            webView.settings,
        //            WebSettingsCompat.FORCE_DARK_ON
        //        )
        //        Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> WebSettingsCompat.setForceDark(
        //            webView.settings,
        //            WebSettingsCompat.FORCE_DARK_OFF
        //        )
        //    }
        // }

        webView.webChromeClient = webViewChromeClient
        webView.webViewClient = webViewClient

        webView.addJavascriptInterface(ReaderJSInterface, ReaderJS.Bridge)

        ReaderJSInterface.renderReadabilityPage(
            webView,
            "https://m.sanyewu.net/94307_94307596/232784104.html"
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

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
            return
        }
        super.onBackPressed()
    }
}
