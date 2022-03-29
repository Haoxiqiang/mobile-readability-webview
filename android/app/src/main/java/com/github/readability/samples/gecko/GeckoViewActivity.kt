package com.github.readability.samples.gecko

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.github.readability.samples.R
import com.github.readability.samples.SampleURLs
import com.github.readability.samples.StyleSheet
import com.github.readability.webview.GeckoReaderJSInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView

class GeckoViewActivity : AppCompatActivity() {

    private val geckoView by lazy { findViewById<GeckoView>(R.id.geckoView) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progressBar) }

    private val session: GeckoSession by lazy {
        val gs = GeckoSession()
        val settings = gs.settings
        settings.allowJavascript = true
        settings.suspendMediaWhenInactive = true
        settings.useTrackingProtection = false
        gs.progressDelegate = createProgressDelegate()
        gs
    }

    private fun createProgressDelegate(): GeckoSession.ProgressDelegate {
        return object : GeckoSession.ProgressDelegate {

            override fun onPageStop(session: GeckoSession, success: Boolean) = Unit

            override fun onSecurityChange(
                session: GeckoSession,
                securityInfo: GeckoSession.ProgressDelegate.SecurityInformation
            ) = Unit

            override fun onPageStart(session: GeckoSession, url: String) = Unit

            override fun onProgressChange(session: GeckoSession, progress: Int) {
                progressBar.progress = progress // 这个是你自己定义的进度条
                if (progress in 1..99) {
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gecko_view)

        findViewById<View>(R.id.more).setOnClickListener {
            SampleURLs.show(this@GeckoViewActivity, urlPicker = { url ->
                GeckoReaderJSInterface.renderReadabilityPage(session, url)
            }, dismiss = {
            })
        }

        findViewById<FloatingActionButton>(R.id.style).setOnClickListener {
            StyleSheet()
                .apply {
                    styleTheme = { st ->
                        GeckoReaderJSInterface.exeJavaScript(session, st)
                    }
                    styleFontSize = { sf ->
                        GeckoReaderJSInterface.exeJavaScript(session, sf)
                    }
                }
                .show(supportFragmentManager, "StyleSheet")
        }

        session.open(GeckoManager.runtime)
        geckoView.setSession(session)

        GeckoReaderJSInterface.renderReadabilityPage(
            session,
            "https://www.zhihu.com/question/47819047/answer/108130984"
        )
    }

    override fun onResume() {
        super.onResume()
        session.setActive(true)
    }

    override fun onPause() {
        super.onPause()
        session.setActive(false)
    }

    override fun onBackPressed() {
        // if (readerViewFeature.onBackPressed()) {
        //    return
        // }
        super.onBackPressed()
    }
}
