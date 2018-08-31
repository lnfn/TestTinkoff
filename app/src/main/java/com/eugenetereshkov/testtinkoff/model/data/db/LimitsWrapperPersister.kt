package com.eugenetereshkov.testtinkoff.model.data.db

import com.eugenetereshkov.testtinkoff.extension.fromJson
import com.google.gson.Gson
import com.j256.ormlite.field.FieldType
import com.j256.ormlite.field.SqlType
import com.j256.ormlite.field.types.StringType


class LimitsWrapperPersister private constructor() : StringType(
        SqlType.STRING,
        arrayOf<Class<*>>(LimitsWrapper::class.java)
) {

    companion object {
        private val INSTANCE = LimitsWrapperPersister()
        @JvmStatic
        fun getSingleton() = INSTANCE
    }

    override fun javaToSqlArg(fieldType: FieldType?, javaObject: Any): String {
        return getJsonFromLimitsWrapper(javaObject as LimitsWrapper)
    }

    override fun sqlArgToJava(fieldType: FieldType?, sqlArg: Any, columnPos: Int): LimitsWrapper {
        return getLimitsWrapperFromJson(sqlArg as String)
    }

    private fun getJsonFromLimitsWrapper(limitsWrapper: LimitsWrapper): String {
        return Gson().toJson(limitsWrapper)
    }

    private fun getLimitsWrapperFromJson(string: String): LimitsWrapper {
        return Gson().fromJson<LimitsWrapper>(string)
    }
}
