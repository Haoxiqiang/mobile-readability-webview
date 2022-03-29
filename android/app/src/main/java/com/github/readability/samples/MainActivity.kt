package com.github.readability.samples

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.readability.samples.gecko.GeckoViewActivity
import com.github.readability.samples.webview.WebViewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.webView).setOnClickListener {
            startActivity(Intent(this@MainActivity, WebViewActivity::class.java))
        }

        findViewById<View>(R.id.geckoView).setOnClickListener {
            startActivity(Intent(this@MainActivity, GeckoViewActivity::class.java))
        }
    }
}
