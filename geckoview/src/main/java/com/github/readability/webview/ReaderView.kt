/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.github.readability.webview

import org.json.JSONObject
import java.util.Locale

object ReaderView {
    // Name of the port connected to all pages for checking whether or not
    // a page is readerable (see readerview_content.js).
    const val READER_VIEW_CONTENT_PORT = "mozacReaderview"

    // Name of the port connected to active reader pages for updating
    // appearance configuration (see readerview.js).
    const val READER_VIEW_EXTENSION_ID = "readerview@mozac.org"
    const val READER_VIEW_ACTIVE_CONTENT_PORT = "mozacReaderviewActive"
    const val READER_VIEW_EXTENSION_URL =
        "resource://android/assets/extensions/readerview/"

    // Constants for building messages sent to the web extension:
    // Change the font type: {"action": "setFontType", "value": "sans-serif"}
    // Show reader view: {"action": "show", "value": {"fontSize": 3, "fontType": "serif", "colorScheme": "dark"}}
    const val ACTION_MESSAGE_KEY = "action"
    const val ACTION_SHOW = "show"
    const val ACTION_HIDE = "hide"
    const val ACTION_CHECK_READER_STATE = "checkReaderState"
    const val ACTION_SET_COLOR_SCHEME = "setColorScheme"
    const val ACTION_CHANGE_FONT_SIZE = "changeFontSize"
    const val ACTION_SET_FONT_TYPE = "setFontType"
    const val ACTION_VALUE = "value"
    const val ACTION_VALUE_SHOW_FONT_SIZE = "fontSize"
    const val ACTION_VALUE_SHOW_FONT_TYPE = "fontType"
    const val ACTION_VALUE_SHOW_COLOR_SCHEME = "colorScheme"
    const val READERABLE_RESPONSE_MESSAGE_KEY = "readerable"
    const val BASE_URL_RESPONSE_MESSAGE_KEY = "baseUrl"
    const val ACTIVE_URL_RESPONSE_MESSAGE_KEY = "activeUrl"

    // Constants for storing the reader mode config in shared preferences
    const val SHARED_PREF_NAME = "mozac_feature_reader_view"
    const val COLOR_SCHEME_KEY = "mozac-readerview-colorscheme"
    const val FONT_TYPE_KEY = "mozac-readerview-fonttype"
    const val FONT_SIZE_KEY = "mozac-readerview-fontsize"
    const val FONT_SIZE_DEFAULT = 3

    enum class FontType(val value: String) { SANSSERIF("sans-serif"), SERIF("serif") }
    enum class ColorScheme { LIGHT, SEPIA, DARK }

    fun createCheckReaderStateMessage(): JSONObject {
        return JSONObject().put(ACTION_MESSAGE_KEY, ACTION_CHECK_READER_STATE)
    }

    fun createShowReaderMessage(): JSONObject {
        val fontSize = FONT_SIZE_DEFAULT
        val fontType = FontType.SERIF
        val colorScheme = ColorScheme.LIGHT
        val configJson = JSONObject()
            .put(ACTION_VALUE_SHOW_FONT_SIZE, fontSize)
            .put(
                ACTION_VALUE_SHOW_FONT_TYPE,
                fontType.value.lowercase(Locale.ROOT)
            )
            .put(
                ACTION_VALUE_SHOW_COLOR_SCHEME,
                colorScheme.name.lowercase(Locale.ROOT)
            )

        return JSONObject()
            .put(ACTION_MESSAGE_KEY, ACTION_SHOW)
            .put(ACTION_VALUE, configJson)
    }

    fun createHideReaderMessage(): JSONObject {
        return JSONObject().put(ACTION_MESSAGE_KEY, ACTION_HIDE)
    }
}
