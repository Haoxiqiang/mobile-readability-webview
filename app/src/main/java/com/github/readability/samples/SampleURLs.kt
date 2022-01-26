package com.github.readability.samples

import android.app.Activity
import androidx.appcompat.app.AlertDialog

object SampleURLs {
    private val urls = arrayOf(
        "https://mp.weixin.qq.com/s/yk3FCuaU9LtmdM1uOQjOMQ",
        "https://www.zhihu.com/question/47819047/answer/108130984",
        "https://36kr.com/p/1578350401997569",
    )

    fun show(activity: Activity, urlPicker: (String) -> Unit) {
        AlertDialog.Builder(activity)
            .setItems(urls) { dialog, index ->
                urlPicker.invoke(urls[index])
                dialog.dismiss()
            }
            .show()
    }

}