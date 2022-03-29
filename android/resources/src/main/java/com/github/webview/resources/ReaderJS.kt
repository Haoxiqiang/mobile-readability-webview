package com.github.webview.resources

import org.json.JSONObject

interface ReaderJS {
    companion object {
        const val Bridge = "readability"
        const val ACTION_MESSAGE_KEY = "action"
        const val ACTION_SET_COLOR_SCHEME = "setColorScheme"
        const val ACTION_VALUE_COLOR_SCHEME = "colorScheme"

        const val ACTION_CHANGE_FONT_SIZE = "changeFontSize"
        const val ACTION_VALUE_FONT_SIZE = "fontSize"

        const val ACTION_SET_FONT_TYPE = "setFontType"
        const val ACTION_VALUE_FONT_TYPE = "fontType"

        var theme: JSONObject = JSONObject()
    }
}
