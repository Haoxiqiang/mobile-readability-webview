package com.github.readability.samples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.github.readability.webview.ReaderJSInterface
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONObject

fun DialogFragment.bind(bundle: Bundle.() -> Unit): DialogFragment {
    var args: Bundle? = arguments
    if (args == null) {
        args = Bundle()
        arguments = args
    }
    bundle.invoke(args)
    return this
}

class StyleSheet : BottomSheetDialogFragment() {

    var styleTheme: ((JSONObject) -> Unit)? = null
    var styleFontSize: ((JSONObject) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sheet_view_style, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.dark).setOnClickListener {
            themeClick("dark")
        }
        view.findViewById<View>(R.id.light).setOnClickListener {
            themeClick("light")
        }
        view.findViewById<View>(R.id.sepia).setOnClickListener {
            themeClick("sepia")
        }
    }

    private fun themeClick(t: String) {
        val json = JSONObject()
        json.put(ReaderJSInterface.ACTION_MESSAGE_KEY, ReaderJSInterface.ACTION_SET_COLOR_SCHEME)
        json.put(ReaderJSInterface.ACTION_VALUE_COLOR_SCHEME, t)
        styleTheme?.invoke(json)
    }
}
