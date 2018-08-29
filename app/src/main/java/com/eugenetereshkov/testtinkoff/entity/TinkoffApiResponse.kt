package com.eugenetereshkov.testtinkoff.entity


abstract class TinkoffApiResponse<T> {
    val resultCode: String = ""
    abstract val payload: T
}
