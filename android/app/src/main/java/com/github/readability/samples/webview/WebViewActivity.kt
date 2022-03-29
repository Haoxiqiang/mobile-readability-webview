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
import com.github.readability.samples.R
import com.github.readability.samples.SampleURLs
import com.github.readability.samples.StyleSheet
import com.github.readability.webview.ReaderJSInterface
import com.github.webview.resources.ReaderJS
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
            return super.shouldOverrideUrlLoading(view, url)
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && webView.settings.forceDark == WebSettingsCompat.FORCE_DARK_ON) {
                            Toast.makeText(
                                applicationContext,
                                "webView.settings.forceDark, theme won't works.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            ReaderJSInterface.exeJavaScript(webView, st)
                        }
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
            "https://www.zhihu.com/question/47819047/answer/108130984"
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
