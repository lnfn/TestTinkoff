package com.eugenetereshkov.testtinkoff.model.data.db

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.eugenetereshkov.testtinkoff.entity.DepositionPoint
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.field.DataPersisterManager
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import timber.log.Timber


class DatabaseHelper(
        context: Context
) : OrmLiteSqliteOpenHelper(
        context, DATABASE_NAME,
        null,
        DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "testtinkoff.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, DepositionPoint::class.java)
            TableUtils.createTableIfNotExists(connectionSource, DepositionPartnerEntity::class.java)

            DataPersisterManager.registerDataPersisters(LocationPersister.getSingleton())
            DataPersisterManager.registerDataPersisters(LimitsWrapperPersister.getSingleton())
        } catch (e: SQLException) {
            Timber.e("error creating DB $DATABASE_NAME")
            throw RuntimeException(e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, connectionSource: ConnectionSource, oldVer: Int, newVer: Int) {
        TableUtils.dropTable<DepositionPoint, Any>(connectionSource, DepositionPoint::class.java, true)
        TableUtils.dropTable<DepositionPartnerEntity, Any>(connectionSource, DepositionPartnerEntity::class.java, true)
        onCreate(db, connectionSource)
    }
}
