package com.eugenetereshkov.testtinkoff.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Currency(
        val name: String,
        val code: Int
) : Parcelable
