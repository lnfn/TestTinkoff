package com.eugenetereshkov.testtinkoff.model.data.db

import com.eugenetereshkov.testtinkoff.entity.Location
import com.eugenetereshkov.testtinkoff.extension.fromJson
import com.google.gson.Gson
import com.j256.ormlite.field.FieldType
import com.j256.ormlite.field.SqlType
import com.j256.ormlite.field.types.StringType


class LocationPersister private constructor() : StringType(
        SqlType.STRING,
        arrayOf<Class<*>>(Location::class.java)
) {

    companion object {
        private val INSTANCE = LocationPersister()
        @JvmStatic
        fun getSingleton() = INSTANCE
    }

    override fun javaToSqlArg(fieldType: FieldType?, javaObject: Any): Any {
        return getJsonFromLocationClass(javaObject as Location)
    }

    override fun sqlArgToJava(fieldType: FieldType?, sqlArg: Any, columnPos: Int): Any {
        return getLocationClassFromJson(sqlArg as String)
    }

    private fun getJsonFromLocationClass(location: Location): String {
        return Gson().toJson(location)
    }

    private fun getLocationClassFromJson(string: String): Location {
        return Gson().fromJson<Location>(string)
    }
}
