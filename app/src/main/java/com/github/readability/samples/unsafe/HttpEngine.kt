package com.github.readability.samples.unsafe

import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import androidx.annotation.RequiresApi
import okhttp3.Call
import okhttp3.Headers.Companion.toHeaders
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

object HttpEngine {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        )
        .addInterceptor(OkHttpWebResponse())
        .build()

    var userAgent: String = ""

    val prepareSet = hashSetOf<String>()

    fun handleRequestViaOkHttp(url: String): WebResourceResponse? {
        if (!prepareSet.contains(url)) {
            return null
        }
        return try {
            val call: Call = client.newCall(
                Request.Builder()
                    .url(url)
                    .header("User-Agent", userAgent)
                    .build()
            )
            val response: Response = call.execute()
            val responseBody = response.body ?: return null
            val headers = response.headers.toMultimap()
                .map { entry -> Pair(entry.key, entry.value.joinToString(separator = ";")) }
                .toMap()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val reasonPhrase = if (response.message.isEmpty()) {
                    response.code.toString()
                } else {
                    response.message
                }
                WebResourceResponse(
                    "text/html", "utf-8", response.code, reasonPhrase,
                    headers,
                    responseBody.byteStream()
                )
            } else {
                WebResourceResponse("text/html", "utf-8", responseBody.byteStream())
            }
        } catch (e: Exception) {
            // return response for bad request
            e.printStackTrace()
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun handleRequestViaOkHttp(webResourceRequest: WebResourceRequest?): WebResourceResponse? {
        return webResourceRequest?.let { webRequest ->
            val url = webRequest.url.toString()
            if (!prepareSet.contains(url)) {
                return null
            }
            return try {
                val call: Call = client.newCall(
                    Request.Builder()
                        .url(url)
                        .method(webRequest.method, null)
                        .headers(webRequest.requestHeaders.toHeaders())
                        .header("User-Agent", userAgent)
                        .build()
                )
                val response: Response = call.execute()
                val responseBody = response.body ?: return null
                val headers = response.headers.toMultimap()
                    .map { entry -> Pair(entry.key, entry.value.joinToString(separator = ";")) }
                    .toMap()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val reasonPhrase = if (response.message.isEmpty()) {
                        response.code.toString()
                    } else {
                        response.message
                    }
                    WebResourceResponse(
                        "text/html", "utf-8", response.code, reasonPhrase,
                        headers,
                        responseBody.byteStream()
                    )
                } else {
                    WebResourceResponse("text/html", "utf-8", responseBody.byteStream())
                }
            } catch (e: Exception) {
                // return response for bad request
                e.printStackTrace()
                null
            }
        }
    }
}
