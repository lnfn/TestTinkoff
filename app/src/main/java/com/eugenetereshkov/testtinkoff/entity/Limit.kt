package com.eugenetereshkov.testtinkoff.entity

import java.io.Serializable


data class Limit(
        val currency: Currency,
        val min: Int,
        val max: Int
) : Serializable
