package com.eugenetereshkov.testtinkoff.entity


data class DepositionPartner(
        val id: String,
        val name: String,
        val picture: String,
        val url: String,
        val hasLocations: Boolean,
        val isMomentary: Boolean,
        val depositionDuration: String,
        val limitations: String,
        val pointType: String,
        val externalPartnerId: String,
        val description: String,
        val moneyMin: Int,
        val moneyMax: Int,
        val hasPreferentialDeposition: Boolean,
        val limits: List<Limit>,
        val dailyLimits: List<DailyLimit>
)
