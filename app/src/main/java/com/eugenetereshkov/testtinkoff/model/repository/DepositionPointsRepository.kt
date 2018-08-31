package com.eugenetereshkov.testtinkoff.model.repository

import com.eugenetereshkov.testtinkoff.entity.DepositionPartner
import com.eugenetereshkov.testtinkoff.entity.DepositionPartnersResponse
import com.eugenetereshkov.testtinkoff.entity.DepositionPoint
import com.eugenetereshkov.testtinkoff.entity.DepositionPointsResponse
import com.eugenetereshkov.testtinkoff.entity.TargetMapPosition
import com.eugenetereshkov.testtinkoff.model.data.api.TinkoffApi
import com.eugenetereshkov.testtinkoff.model.data.db.DepositionPartnerDao
import com.eugenetereshkov.testtinkoff.model.data.db.DepositionPartnerEntity
import com.eugenetereshkov.testtinkoff.model.data.db.DepositionPointDao
import com.eugenetereshkov.testtinkoff.model.system.SchedulersProvider
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


interface IDepositionPointsRepository {
    val sourceObservable: Observable<List<DepositionPoint>>
    val request: PublishSubject<TargetMapPosition>
}

@Singleton
class DepositionPointsRepository @Inject constructor(
        private val api: TinkoffApi,
        private val appSchedulers: SchedulersProvider,
        private val depositionPointDao: DepositionPointDao,
        private val depositionPartnersDao: DepositionPartnerDao
) : IDepositionPointsRepository {

    private val source = BehaviorSubject.create<List<DepositionPoint>>()

    override val request = PublishSubject.create<TargetMapPosition>()
    override val sourceObservable: Observable<List<DepositionPoint>> = source.hide()

    private var isPartnerDb = true
    private var isPointDB = false

    init {
        request.hide()
                .debounce(300, TimeUnit.MILLISECONDS)
                .switchMap { getDepositionPointsAndPartners(it) }
                .flatMap { getDepositionPointsFromDb().toObservable() }
                .observeOn(appSchedulers.ui())
                .subscribe(source)
    }

    private fun getDepositionPointsAndPartners(position: TargetMapPosition): Observable<Unit> {
        return Observable.zip(
                refreshDepositionPintsDB(position).toObservable(),
                refreshDepositionPartnersDB().toObservable(),
                BiFunction { _, _ -> Unit }
        )
    }

    private fun refreshDepositionPintsDB(position: TargetMapPosition): Single<Int> {
        return getDepositionPointsFromNetwork(position)
                .map { it.payload }
                .map { depositionPointDao.refresh(it) }
    }

    private fun refreshDepositionPartnersDB(): Single<Int> {
        return getDepositionPartnersFromNetwork()
                .map { it.payload }
                .map { partners ->
                    partners.map { DepositionPartner.convertToDepositionPartnerEntity(it) }
                }
                .map { depositionPartnersDao.refresh(it) }
    }

    private fun getDepositionPointsFromNetwork(
            targetMapPosition: TargetMapPosition
    ): Single<DepositionPointsResponse> {
        return api.getDepositionPoints(
                targetMapPosition.latitude,
                targetMapPosition.longitude,
                targetMapPosition.radius
        )
                .subscribeOn(appSchedulers.io())
    }

    private fun getDepositionPointsFromDb(): Single<List<DepositionPoint>> {
        return Single.defer {
            Single.just(depositionPointDao.getAll())
        }
                .subscribeOn(appSchedulers.io())
    }

    private fun getDepositionPartnersFromNetwork(): Single<DepositionPartnersResponse> {
        return api.getDepositionPartners()
                .subscribeOn(appSchedulers.io())
    }

    private fun getDepositionParthnersFromDB(): Single<List<DepositionPartnerEntity>> {
        return Single.defer {
            Single.just(depositionPartnersDao.getAll())
        }
                .subscribeOn(appSchedulers.io())
    }
}
