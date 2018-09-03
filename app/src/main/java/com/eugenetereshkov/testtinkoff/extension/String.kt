package com.eugenetereshkov.testtinkoff.extension

import android.content.Context
import android.os.Build
import android.text.Html
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner


@Suppress("DEPRECATION")
fun String.fromHtml(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
} else {
    Html.fromHtml(this).toString()
}


fun String.getDownloadImageUrl(context: Context): String {
    return "${DepositionPointAndPartner.IMAGE_URL}${context.resources.displayMetrics.densityDpi.getDensityName()}/${this}"
}
