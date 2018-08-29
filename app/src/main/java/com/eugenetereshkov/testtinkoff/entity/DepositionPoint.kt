package com.eugenetereshkov.testtinkoff.entity


data class DepositionPoint(
        val externalId: String,
        val partnerName: String,
        val location: Location,
        val addressInfo: String,
        val fullAddress: String,
        val verificationInfo: String
)
