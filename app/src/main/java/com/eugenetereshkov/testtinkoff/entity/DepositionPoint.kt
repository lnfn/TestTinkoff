package com.eugenetereshkov.testtinkoff.entity

import com.eugenetereshkov.testtinkoff.model.data.db.LocationPersister
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable


@DatabaseTable(tableName = "deposition_points")
class DepositionPoint {
    @DatabaseField(generatedId = true)
    val id: Int = 0
    @DatabaseField
    val externalId: String = ""
    @DatabaseField
    val partnerName: String = ""
    @DatabaseField(persisterClass = LocationPersister::class)
    val location: Location = Location(0.0, 0.0)
    @DatabaseField
    val workHours: String = ""
    @DatabaseField
    val addressInfo: String = ""
    @DatabaseField
    val fullAddress: String = ""
    @DatabaseField
    val verificationInfo: String = ""
}
