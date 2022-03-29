package com.github.readability.samples.gecko

import com.github.readability.samples.App
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoRuntimeSettings

object GeckoManager {

    val runtime by lazy {
        val runtimeSettings = GeckoRuntimeSettings.Builder()
            .remoteDebuggingEnabled(true)
            .javaScriptEnabled(true)
            .aboutConfigEnabled(true)
            .consoleOutput(true)
            .debugLogging(true)
            .webFontsEnabled(true)
            .allowInsecureConnections(GeckoRuntimeSettings.ALLOW_ALL)
            .build()
        GeckoRuntime.create(App.get(), runtimeSettings)
    }
}
