package com.eugenetereshkov.testtinkoff.model.data.db

import android.content.Context
import com.j256.ormlite.android.apptools.OpenHelperManager
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class HelperFactory @Inject constructor(
        context: Context
) {
    var databaseHelper: DatabaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper::class.java)

    fun releaseHelper() {
        OpenHelperManager.releaseHelper()
    }
}
