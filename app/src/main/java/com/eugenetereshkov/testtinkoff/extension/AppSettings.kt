package com.eugenetereshkov.testtinkoff.extension

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import com.eugenetereshkov.testtinkoff.BuildConfig
import com.eugenetereshkov.testtinkoff.R


fun Fragment.showSettingsRequest(@StringRes messageId: Int) {
    view?.let { view ->
        Snackbar.make(view, messageId, Snackbar.LENGTH_LONG)
                .setAction(R.string.settings) { openSettings() }
                .show()
    }
}

fun Fragment.openSettings() {
    startActivity(getSettingsIntent())
}

private fun getSettingsIntent(): Intent = Intent().apply {
    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
    flags = Intent.FLAG_ACTIVITY_NEW_TASK
}
