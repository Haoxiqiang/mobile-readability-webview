package com.github.readability.samples

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.github.readability.samples.unsafe.HttpEngine
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView

class GeckoViewActivity : AppCompatActivity() {

    private val geckoView by lazy { findViewById<GeckoView>(R.id.geckoView) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progressBar) }

    private val runtime by lazy {
        GeckoRuntime.create(this@GeckoViewActivity)
    }
    private val session: GeckoSession by lazy {
        val gs = GeckoSession()
        gs.open(runtime)
        gs
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gecko_view)

        findViewById<View>(R.id.loadURLs).setOnClickListener {
            SampleURLs.show(this@GeckoViewActivity) { url ->
                HttpEngine.prepareSet.add(url)
                session.loadUri(url)
            }
        }

        findViewById<View>(R.id.readability).setOnClickListener {
            // ReadabilityJSInject.readabilityToggle(webView)
        }

        // session.loadUri("about:buildconfig")
        session.loadUri("https://www.zhihu.com/question/47819047/answer/108130984")

//        webView.loadUrl(
//            "file:///android_asset/readerview/readerview.html?ref=${
//                URLEncoder.encode(
//                    "https://www.zhihu.com/question/47819047/answer/108130984",
//                    "UTF-8"
//                )
//            }"
//        )
    }

    override fun onResume() {
        super.onResume()
        // geckoView.resumeTimers()
    }

    override fun onPause() {
        super.onPause()
        // geckoView.pauseTimers()
    }

    override fun onBackPressed() {
//        if (session.canGoBack()) {
//            session.goBack()
//            return
//        }
        super.onBackPressed()
    }
}
