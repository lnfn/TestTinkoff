package com.eugenetereshkov.testtinkoff.model.repository

import com.eugenetereshkov.testtinkoff.entity.DepositionPartner
import com.eugenetereshkov.testtinkoff.entity.DepositionPartnersResponse
import com.eugenetereshkov.testtinkoff.entity.DepositionPoint
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner
import com.eugenetereshkov.testtinkoff.entity.DepositionPointsResponse
import com.eugenetereshkov.testtinkoff.entity.TargetMapPosition
import com.eugenetereshkov.testtinkoff.model.data.api.TinkoffApi
import com.eugenetereshkov.testtinkoff.model.data.db.DepositionPartnerDao
import com.eugenetereshkov.testtinkoff.model.data.db.DepositionPartnerEntity
import com.eugenetereshkov.testtinkoff.model.data.db.DepositionPointDao
import com.eugenetereshkov.testtinkoff.model.system.AppPreferences
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
    val sourceObservable: Observable<List<DepositionPointAndPartner>>
    val request: PublishSubject<TargetMapPosition>
}

@Singleton
class DepositionPointsRepository @Inject constructor(
        private val api: TinkoffApi,
        private val appSchedulers: SchedulersProvider,
        private val depositionPointDao: DepositionPointDao,
        private val depositionPartnersDao: DepositionPartnerDao,
        private val appPreferences: AppPreferences
) : IDepositionPointsRepository {

    companion object {
        const val TIME_TO_UPDATE = 10
    }

    private val source = BehaviorSubject.create<List<DepositionPointAndPartner>>()

    override val request = PublishSubject.create<TargetMapPosition>()
    override val sourceObservable: Observable<List<DepositionPointAndPartner>> = source.hide()

    init {
        request.hide()
                .debounce(300, TimeUnit.MILLISECONDS)
                .switchMap(this::getDepositionPointsAndPartnersObservable)
                .flatMap { getJoinDepositionPintsAndPartnerObservable() }
                .observeOn(appSchedulers.ui())
                .subscribe(source)
    }

    private fun getDifferentTimeLastUpdatePartners() = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - appPreferences.lastTimeUpdateDepositionPartners)

    private fun isDifferentLastMapPosition(value: TargetMapPosition) = appPreferences.lastMapPosition != value

    private fun getDepositionPointsAndPartnersObservable(position: TargetMapPosition): Observable<Unit> {
        return Observable.defer {
            Observable.zip(
                    if (getDifferentTimeLastUpdatePartners() >= TIME_TO_UPDATE) refreshDepositionPartnersDBSingle().toObservable() else Observable.just(0),
                    if (isDifferentLastMapPosition(position)) refreshDepositionPointsDBSingle(position).toObservable() else Observable.just(0),
                    BiFunction<Int, Int, Unit> { _, _ ->
                        appPreferences.lastMapPosition = position
                        Unit
                    }
            )
        }
    }

    private fun getJoinDepositionPintsAndPartnerObservable(): Observable<List<DepositionPointAndPartner>> {
        return Observable.zip(
                getDepositionPointsFromDbSingle().toObservable(),
                getDepositionPartnersFromDBSingle().toObservable(),
                BiFunction<List<DepositionPoint>, List<DepositionPartnerEntity>, List<DepositionPointAndPartner>> { points, partners ->
                    DepositionPointAndPartner.joinDepositionPointsAndPartners(points, partners)
                }
        )
    }

    private fun refreshDepositionPointsDBSingle(position: TargetMapPosition): Single<Int> {
        return getDepositionPointsFromNetworkSingle(position)
                .map { it.payload }
                .map { depositionPointDao.refresh(it) }
    }

    private fun refreshDepositionPartnersDBSingle(): Single<Int> {
        return getDepositionPartnersFromNetworkSingle()
                .map { it.payload }
                .map { partners ->
                    partners.map { DepositionPartner.convertToDepositionPartnerEntity(it) }
                }
                .map { depositionPartnersDao.refresh(it) }
                .doOnEvent { _, _ -> appPreferences.lastTimeUpdateDepositionPartners = System.currentTimeMillis() }
    }

    private fun getDepositionPointsFromNetworkSingle(
            targetMapPosition: TargetMapPosition
    ): Single<DepositionPointsResponse> {
        return api.getDepositionPoints(
                targetMapPosition.latitude,
                targetMapPosition.longitude,
                targetMapPosition.radius
        )
                .subscribeOn(appSchedulers.io())
                .onErrorReturn { DepositionPointsResponse(emptyList()) }
    }

    private fun getDepositionPointsFromDbSingle(): Single<List<DepositionPoint>> {
        return Single.defer {
            Single.just(depositionPointDao.getAll())
        }
                .subscribeOn(appSchedulers.io())
    }

    private fun getDepositionPartnersFromDBSingle(): Single<List<DepositionPartnerEntity>> {
        return Single.defer {
            Single.just(depositionPartnersDao.getAll())
        }
                .subscribeOn(appSchedulers.io())
    }

    private fun getDepositionPartnersFromNetworkSingle(): Single<DepositionPartnersResponse> {
        return api.getDepositionPartners()
                .subscribeOn(appSchedulers.io())
                .onErrorReturn { DepositionPartnersResponse(emptyList()) }
    }
}
