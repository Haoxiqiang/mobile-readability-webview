package com.github.readability.samples.unsafe

import com.github.readability.samples.App
import com.github.readability.webview.ReadabilityJSInject
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class OkHttpWebResponse : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val url = originRequest.url().toString()
        val origin = chain.proceed(originRequest)

        val responseBody = origin.body()
        val responseString = responseBody?.string()
        val contentType: MediaType? = responseBody?.contentType()

        if (responseString.isNullOrEmpty()) {
            return origin.newBuilder()
                .body(ResponseBody.create(contentType, ""))
                .build()
        }

        val body: ResponseBody = if (HttpEngine.prepareSet.contains(url)) {
            val doc: Document = Jsoup.parse(responseString)
//            doc.head().append(
//                """
//                <meta http-equiv="Content-Security-Policy" content="default-src * gap:; script-src * 'unsafe-inline' 'unsafe-eval'; connect-src *; style-src * 'unsafe-inline';">
//            """.trimIndent()
//            )

            //doc.allElements.attr("crossorigin", "")
            val script = doc.createElement("script")
            script.id("readability-script")
            script.attr("type", "text/javascript")
            script.text(ReadabilityJSInject.decodeReadabilityJS(App.get()))
            script.appendTo(doc.body())
            ResponseBody.create(contentType, doc.toString())
        } else {
            ResponseBody.create(contentType, responseString)
        }

        return origin.newBuilder()
            .body(body)
//            .header("Access-Control-Allow-Origin", "*")
//            .removeHeader("Content-Security-Policy")
//            .header("X-XSS-Protection", "0")
            .build()
    }

}