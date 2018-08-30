package com.eugenetereshkov.testtinkoff.model.repository

import com.eugenetereshkov.testtinkoff.entity.DepositionPartnersResponse
import com.eugenetereshkov.testtinkoff.entity.DepositionPoint
import com.eugenetereshkov.testtinkoff.entity.DepositionPointsResponse
import com.eugenetereshkov.testtinkoff.model.data.api.TinkoffApi
import com.eugenetereshkov.testtinkoff.model.system.SchedulersProvider
import com.eugenetereshkov.testtinkoff.ui.depositionpointsmap.DepositionPointsMapFragment
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


interface IDepositionPointsRepository {

    val source: BehaviorSubject<List<DepositionPoint>>
    val request: PublishSubject<DepositionPointsMapFragment.TargetMapPosition>

    fun getDepositionPoints(
            targetMapPosition: DepositionPointsMapFragment.TargetMapPosition
    ): Single<DepositionPointsResponse>

    fun getDepositionPartners(): Single<DepositionPartnersResponse>

    fun getData(
            latitude: Double,
            longitude: Double,
            radius: Int
    )
}

@Singleton
class DepositionPointsRepository @Inject constructor(
        private val api: TinkoffApi,
        private val appSchedulers: SchedulersProvider
) : IDepositionPointsRepository {

    override val source = BehaviorSubject.create<List<DepositionPoint>>()
    override val request = PublishSubject.create<DepositionPointsMapFragment.TargetMapPosition>()

    init {
        request.hide()
                .debounce(300, TimeUnit.MILLISECONDS)
                .switchMapSingle(this::getDepositionPoints)
                .map { it.payload }
                .subscribe(source)
    }

    override fun getDepositionPoints(
            targetMapPosition: DepositionPointsMapFragment.TargetMapPosition
    ): Single<DepositionPointsResponse> =
            api.getDepositionPoints(
                    targetMapPosition.latitude,
                    targetMapPosition.longitude,
                    targetMapPosition.radius
            )
                    .subscribeOn(appSchedulers.io())
                    .observeOn(appSchedulers.ui())

    override fun getDepositionPartners(): Single<DepositionPartnersResponse> =
            api.getDepositionPartners()
                    .subscribeOn(appSchedulers.io())
                    .observeOn(appSchedulers.ui())

    override fun getData(
            latitude: Double,
            longitude: Double,
            radius: Int
    ) {
//        if (disposable?.isDisposed == false) disposable?.dispose()
//
//        disposable = getDepositionPoints(
//                latitude = latitude,
//                longitude = longitude,
//                radius = radius
//        )
//                .map { it.payload }
//                .subscribe(Consumer { source.onNext(it) })

    }
}
