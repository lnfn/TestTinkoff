package com.eugenetereshkov.testtinkoff.model.data.db

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.table.TableUtils
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DepositionPartnerDao @Inject constructor(
        private val dao: Dao<DepositionPartnerEntity, Int>
) {

    fun refresh(list: List<DepositionPartnerEntity>): Int {
        TableUtils.clearTable(dao.connectionSource, DepositionPartnerEntity::class.java)
        return dao.create(list)
    }

    fun getAll(): List<DepositionPartnerEntity> = dao.queryForAll()
}
