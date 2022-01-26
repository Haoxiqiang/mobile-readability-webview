package com.github.readability.samples

import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        private var application: Application? = null

        @JvmStatic
        fun get(): Context {
            return application!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}