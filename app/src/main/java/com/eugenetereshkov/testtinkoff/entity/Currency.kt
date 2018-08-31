package com.eugenetereshkov.testtinkoff.entity

import java.io.Serializable


data class Currency(
        val name: String,
        val code: Int
) : Serializable
