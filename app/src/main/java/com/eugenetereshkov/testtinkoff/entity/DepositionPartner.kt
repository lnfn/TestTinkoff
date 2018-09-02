package com.eugenetereshkov.testtinkoff.entity

import com.eugenetereshkov.testtinkoff.model.data.db.DepositionPartnerEntity
import com.eugenetereshkov.testtinkoff.model.data.db.LimitsWrapper


class DepositionPartner(
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
) {
    companion object {
        fun convertToDepositionPartnerEntity(partner: DepositionPartner) = DepositionPartnerEntity().apply {
            id = partner.id
            name = partner.name
            picture = partner.picture
            url = partner.url
            hasLocations = partner.hasLocations
            isMomentary = partner.isMomentary
            depositionDuration = partner.depositionDuration
            limitations = partner.limitations
            pointType = partner.pointType
            externalPartnerId = partner.externalPartnerId
            description = partner.description
            moneyMin = partner.moneyMin
            moneyMax = partner.moneyMax
            hasPreferentialDeposition = partner.hasPreferentialDeposition

            limitsWrapper = LimitsWrapper().apply {
                limits = partner.limits
                dailyLimits = partner.dailyLimits
            }
        }
    }
}
