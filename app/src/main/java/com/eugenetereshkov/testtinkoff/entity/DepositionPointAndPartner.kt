package com.eugenetereshkov.testtinkoff.entity

import com.eugenetereshkov.testtinkoff.model.data.db.DepositionPartnerEntity
import com.eugenetereshkov.testtinkoff.model.data.db.LimitsWrapper


class DepositionPointAndPartner(
        val id: Int,
        val location: Location,
        val addressInfo: String,
        val fullAddress: String,
        val verificationInfo: String,
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
        val limitsWrapper: LimitsWrapper
) {
    companion object {
        fun joinDepositionPointsAndPartners(
                points: List<DepositionPoint>,
                partners: List<DepositionPartnerEntity>
        ): List<DepositionPointAndPartner> {
            val partnersMap = partners.associateBy { it.id }
            return points.map { point ->
                val partner = partnersMap[point.partnerName] as DepositionPartnerEntity
                DepositionPointAndPartner(
                        id = point.id,
                        location = point.location,
                        addressInfo = point.addressInfo,
                        fullAddress = point.fullAddress,
                        verificationInfo = point.verificationInfo,
                        name = partner.name,
                        picture = partner.picture,
                        url = partner.url,
                        hasLocations = partner.hasLocations,
                        isMomentary = partner.isMomentary,
                        depositionDuration = partner.depositionDuration,
                        limitations = partner.limitations,
                        pointType = partner.pointType,
                        externalPartnerId = partner.externalPartnerId,
                        description = partner.description,
                        moneyMin = partner.moneyMin,
                        moneyMax = partner.moneyMax,
                        hasPreferentialDeposition = partner.hasPreferentialDeposition,
                        limitsWrapper = partner.limitsWrapper
                )
            }
        }
    }
}
