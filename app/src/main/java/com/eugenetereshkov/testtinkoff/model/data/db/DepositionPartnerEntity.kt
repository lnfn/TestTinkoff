package com.eugenetereshkov.testtinkoff.model.data.db

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable


@DatabaseTable(tableName = "deposition_partners")
class DepositionPartnerEntity {
    @DatabaseField(generatedId = true)
    val _id: Int = 0
    @DatabaseField(unique = true)
    var id: String = ""
    @DatabaseField
    var name: String = ""
    @DatabaseField
    var picture: String = ""
    @DatabaseField
    var url: String = ""
    @DatabaseField
    var hasLocations: Boolean = false
    @DatabaseField
    var isMomentary: Boolean = false
    @DatabaseField
    var depositionDuration: String = ""
    @DatabaseField
    var limitations: String = ""
    @DatabaseField
    var pointType: String = ""
    @DatabaseField
    var externalPartnerId: String = ""
    @DatabaseField
    var description: String = ""
    @DatabaseField
    var moneyMin: Int = 0
    @DatabaseField
    var moneyMax: Int = 0
    @DatabaseField
    var hasPreferentialDeposition: Boolean = false
    @DatabaseField(persisterClass = LimitsWrapperPersister::class)
    var limitsWrapper: LimitsWrapper = LimitsWrapper()
}
