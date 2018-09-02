package com.eugenetereshkov.testtinkoff.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Limit(
        val currency: Currency,
        val min: Int,
        val max: Int
) : Parcelable
