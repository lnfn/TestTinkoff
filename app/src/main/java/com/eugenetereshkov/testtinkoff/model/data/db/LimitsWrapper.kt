package com.eugenetereshkov.testtinkoff.model.data.db

import android.os.Parcelable
import com.eugenetereshkov.testtinkoff.entity.DailyLimit
import com.eugenetereshkov.testtinkoff.entity.Limit
import kotlinx.android.parcel.Parcelize


@Parcelize
class LimitsWrapper : Parcelable {
    var limits: List<Limit> = emptyList()
    var dailyLimits: List<DailyLimit> = emptyList()
}
