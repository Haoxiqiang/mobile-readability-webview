package com.github.readability.samples

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.github.readability.samples.unsafe.HttpEngine
import com.github.readability.webview.ReaderView
import org.json.JSONException
import org.json.JSONObject
import org.mozilla.geckoview.AllowOrDeny
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoRuntimeSettings
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView
import org.mozilla.geckoview.WebExtension
import org.mozilla.geckoview.WebExtension.MessageDelegate
import org.mozilla.geckoview.WebExtension.MessageSender
import org.mozilla.geckoview.WebExtension.Port
import org.mozilla.geckoview.WebExtension.PortDelegate

class GeckoViewActivity : AppCompatActivity() {

    private val geckoView by lazy { findViewById<GeckoView>(R.id.geckoView) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progressBar) }

    private val runtime by lazy {
        val runtimeSettings = GeckoRuntimeSettings.Builder()
            .remoteDebuggingEnabled(true)
            .build()
        GeckoRuntime.create(this@GeckoViewActivity, runtimeSettings)
    }

    private var mPort: Port? = null
    private val session: GeckoSession by lazy {
        val gs = GeckoSession()
        val settings = gs.settings
        settings.allowJavascript = true
        settings.suspendMediaWhenInactive = true
        settings.useTrackingProtection = false
        gs.progressDelegate = createProgressDelegate()
        gs.navigationDelegate = createNavigationDelegate()
        gs
    }

    private fun createNavigationDelegate(): GeckoSession.NavigationDelegate {
        return object : GeckoSession.NavigationDelegate {
            override fun onLoadRequest(
                session: GeckoSession,
                request: GeckoSession.NavigationDelegate.LoadRequest
            ): GeckoResult<AllowOrDeny> {
                return GeckoResult.fromValue(AllowOrDeny.ALLOW)
            }

            override fun onSubframeLoadRequest(
                session: GeckoSession,
                request: GeckoSession.NavigationDelegate.LoadRequest
            ): GeckoResult<AllowOrDeny> {
                return GeckoResult.fromValue(AllowOrDeny.ALLOW)
            }
        }
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

        findViewById<View>(R.id.loadURLs).setOnClickListener {
            SampleURLs.show(this@GeckoViewActivity) { url ->
                HttpEngine.prepareSet.add(url)
                session.loadUri(url)
            }
        }

        findViewById<View>(R.id.readability).setOnClickListener {
            // ReadabilityJSInject.readabilityToggle(webView)
            mPort?.postMessage(ReaderView.createCheckReaderStateMessage())
            it.postDelayed({
                mPort?.postMessage(ReaderView.createShowReaderMessage())
            }, 2000)
        }

        // session.loadUri("about:buildconfig")
        installExtensions()
        session.open(runtime)
        geckoView.setSession(session)
        session.loadUri("https://www.zhihu.com/question/47819047/answer/108130984")
    }

    private fun installExtensions() {
        val portDelegate: PortDelegate = object : PortDelegate {
            override fun onPortMessage(
                message: Any,
                port: Port
            ) {
                // This method will be called every time a message is sent from the
                // extension through this port. For now, let's just log a
                // message.
                Log.d(
                    "GeckoViewActivity",
                    "Received message from WebExtension: " +
                        message
                )
            }

            override fun onDisconnect(port: Port) {
                Log.d(
                    "GeckoViewActivity", "onDisconnect: port"
                )
                // After this method is called, this port is not usable anymore.
                if (port === mPort) {
                    mPort = null
                }
            }
        }

        val messageDelegate: MessageDelegate = object : MessageDelegate {
            override fun onConnect(port: Port) {
                Log.i(
                    "GeckoViewActivity",
                    "Extension Port: $port"
                )
                mPort = port
                mPort?.setDelegate(portDelegate)
            }

            override fun onMessage(
                nativeApp: String,
                message: Any,
                sender: MessageSender
            ): GeckoResult<Any>? {
                if (message is JSONObject) {
                    try {
                        if (message.has("type") && "WPAManifest" == message.getString("type")) {
                            val manifest = message.getJSONObject("manifest")
                            Log.d("GeckoViewActivity", "Found WPA manifest: $manifest")
                        }
                    } catch (ex: JSONException) {
                        Log.e("GeckoViewActivity", "Invalid manifest", ex)
                    }
                } else {
                    Log.e("GeckoViewActivity", "message:$message")
                }
                return super.onMessage(nativeApp, message, sender)
            }
        }

        val WEBCOMPAT_EXTENSION_ID = "webcompat@mozilla.org"
        val WEBCOMPAT_EXTENSION_URL = "resource://android/assets/extensions/webcompat/"
        runtime.webExtensionController.ensureBuiltIn(
            WEBCOMPAT_EXTENSION_ID,
            WEBCOMPAT_EXTENSION_URL
        )

        runtime.webExtensionController
            .installBuiltIn(
                ReaderView.READER_VIEW_EXTENSION_URL
            )
            .accept(
                { extension: WebExtension? ->
                    Log.i(
                        "GeckoViewActivity",
                        "Extension installed: $extension"
                    )
                    extension?.let {
                        runOnUiThread {
                            session.webExtensionController.setMessageDelegate(
                                extension, messageDelegate,
                                ReaderView.READER_VIEW_CONTENT_PORT
                            )
                        }
                    }
                }
            ) { e: Throwable? ->
                Log.e(
                    "GeckoViewActivity",
                    "Error registering WebExtension",
                    e
                )
            }
    }

    override fun onResume() {
        super.onResume()
        session.setActive(true)
        // geckoView.resumeTimers()
    }

    override fun onPause() {
        super.onPause()
        session.setActive(false)
        // geckoView.pauseTimers()
    }

    override fun onBackPressed() {
        // if (readerViewFeature.onBackPressed()) {
        //    return
        // }
        super.onBackPressed()
    }
}
