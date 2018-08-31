package com.eugenetereshkov.testtinkoff.model.system

import android.content.SharedPreferences
import com.eugenetereshkov.testtinkoff.entity.TargetMapPosition
import com.eugenetereshkov.testtinkoff.extension.fromJson
import com.google.gson.Gson


class AppPreferences(
        private val sharedPreferences: SharedPreferences
) {

    companion object {
        const val APP_PREFERENCES = "app_preferences"
        const val LAST_TIME_UPDATE_DEPOSITION_PARTNERS = "last_time_update_deposition_partners"
        const val LAST_MAP_POSITION = "last_map_position"
    }

    var lastTimeUpdateDepositionPartners: Long
        @Synchronized set(value) = sharedPreferences.edit().putLong(LAST_TIME_UPDATE_DEPOSITION_PARTNERS, value).apply()
        @Synchronized get() = sharedPreferences.getLong(LAST_TIME_UPDATE_DEPOSITION_PARTNERS, 0)

    var lastMapPosition: TargetMapPosition
        @Synchronized set(value) = sharedPreferences.edit().putString(LAST_MAP_POSITION, Gson().toJson(value, TargetMapPosition::class.java)).apply()
        @Synchronized get() = Gson().fromJson<TargetMapPosition>(sharedPreferences.getString(LAST_MAP_POSITION, null))
                ?: TargetMapPosition.DEFAULT_TARGET_POSITION
}
