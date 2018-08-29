package com.eugenetereshkov.testtinkoff.model.repository

import com.eugenetereshkov.testtinkoff.entity.DepositionPartnersResponse
import com.eugenetereshkov.testtinkoff.entity.DepositionPointsResponse
import com.eugenetereshkov.testtinkoff.model.data.api.TinkoffApi
import com.eugenetereshkov.testtinkoff.model.system.SchedulersProvider
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton


interface IDepositionPointsRepository {
    fun getDepositionPoints(
            latitude: Double,
            longitude: Double,
            radius: Int
    ): Single<DepositionPointsResponse>

    fun getDepositionPartners(): Single<DepositionPartnersResponse>
}

@Singleton
class DepositionPointsRepository @Inject constructor(
        private val api: TinkoffApi,
        private val appSchedulers: SchedulersProvider
) : IDepositionPointsRepository {

    override fun getDepositionPoints(
            latitude: Double,
            longitude: Double,
            radius: Int
    ): Single<DepositionPointsResponse> =
            api.getDepositionPoints(latitude, longitude, radius)
                    .subscribeOn(appSchedulers.io())
                    .observeOn(appSchedulers.ui())

    override fun getDepositionPartners(): Single<DepositionPartnersResponse> =
            api.getDepositionPartners()
                    .subscribeOn(appSchedulers.io())
                    .observeOn(appSchedulers.ui())
}
