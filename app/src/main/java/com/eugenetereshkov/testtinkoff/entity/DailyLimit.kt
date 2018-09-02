package com.eugenetereshkov.testtinkoff.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DailyLimit(
        val currency: Currency,
        val amount: Int
) : Parcelable
