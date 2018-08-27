package com.eugenetereshkov.testtinkoff.model.system

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ResourceManager @Inject constructor(
        private val context: Context
) {
    fun getString(id: Int) = context.getString(id)
}
