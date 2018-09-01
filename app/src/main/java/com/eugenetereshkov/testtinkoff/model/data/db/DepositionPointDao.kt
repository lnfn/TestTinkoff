package com.eugenetereshkov.testtinkoff.model.data.db

import com.eugenetereshkov.testtinkoff.entity.DepositionPoint
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.table.TableUtils
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DepositionPointDao @Inject constructor(
        private val pointDao: Dao<DepositionPoint, Int>
) {
    fun refresh(list: List<DepositionPoint>): Int {
        TableUtils.clearTable(pointDao.connectionSource, DepositionPoint::class.java)
        return pointDao.create(list)
    }

    fun getAll(): List<DepositionPoint> = pointDao.queryForAll()
}
