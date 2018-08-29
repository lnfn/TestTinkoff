package com.eugenetereshkov.testtinkoff.model.data.api

import com.eugenetereshkov.testtinkoff.entity.DepositionPartnersResponse
import com.eugenetereshkov.testtinkoff.entity.DepositionPointsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface TinkoffApi {

    companion object {
        const val BASE_URL = "https://api.tinkoff.ru/v1/"
    }

    @GET("deposition_points")
    fun getDepositionPoints(
            @Query("latitude") latitude: Double,
            @Query("longitude") longitude: Double,
            @Query("radius") radius: Int
    ): Single<DepositionPointsResponse>

    @GET("deposition_partners?accountType=Credit")
    fun getDepositionPartners(): Single<DepositionPartnersResponse>
}
