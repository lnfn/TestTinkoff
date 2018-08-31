package com.eugenetereshkov.testtinkoff.model.data.db

import com.eugenetereshkov.testtinkoff.entity.DepositionPoint
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.table.TableUtils
import javax.inject.Inject


class DepositionPointDao @Inject constructor(
        private val dao: Dao<DepositionPoint, Int>
) {

    fun refresh(list: List<DepositionPoint>): Int {
        TableUtils.clearTable(dao.connectionSource, DepositionPoint::class.java)
        return dao.create(list)
    }

    fun getAll(): List<DepositionPoint> = dao.queryForAll()
}
