package com.eugenetereshkov.testtinkoff.extension

import android.os.Build
import android.text.Html


@Suppress("DEPRECATION")
fun String.fromHtml(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
} else {
    Html.fromHtml(this).toString()
}
