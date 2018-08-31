package com.eugenetereshkov.testtinkoff.extension

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, genericType<T>())

inline fun <reified T> genericType() = object : TypeToken<T>() {}.type
