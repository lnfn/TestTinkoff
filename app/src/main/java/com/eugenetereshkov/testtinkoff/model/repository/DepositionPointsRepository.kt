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
    val sourceErrorObservable: Observable<Throwable>
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
    private val sourceError = BehaviorSubject.create<Throwable>()

    override val request = PublishSubject.create<TargetMapPosition>()
    override val sourceObservable: Observable<List<DepositionPointAndPartner>> = source.hide()
    override val sourceErrorObservable: Observable<Throwable> = sourceError.hide()

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
                    if (getDifferentTimeLastUpdatePartners() >= TIME_TO_UPDATE) refreshDepositionPartnersDBSingle() else Observable.just(0),
                    if (isDifferentLastMapPosition(position)) refreshDepositionPointsDBSingle(position) else Observable.just(0),
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

    private fun refreshDepositionPointsDBSingle(position: TargetMapPosition): Observable<Int> {
        return getDepositionPointsFromNetwork(position)
                .map { it.payload }
                .observeOn(appSchedulers.io())
                .map { depositionPointDao.refresh(it) }
    }

    private fun refreshDepositionPartnersDBSingle(): Observable<Int> {
        return getDepositionPartnersFromNetwork()
                .map { it.payload }
                .map { partners ->
                    partners.map { DepositionPartner.convertToDepositionPartnerEntity(it) }
                }
                .observeOn(appSchedulers.io())
                .map { depositionPartnersDao.refresh(it) }
                .doOnNext { appPreferences.lastTimeUpdateDepositionPartners = System.currentTimeMillis() }
    }

    private fun getDepositionPointsFromNetwork(
            targetMapPosition: TargetMapPosition
    ): Observable<DepositionPointsResponse> {
        return api.getDepositionPoints(
                targetMapPosition.latitude,
                targetMapPosition.longitude,
                targetMapPosition.radius
        )
                .subscribeOn(appSchedulers.io())
                .toObservable()
                .materialize()
                .observeOn(appSchedulers.ui())
                .map { notification ->
                    notification.error?.let { sourceError.onNext(it) }
                    notification
                }
                .observeOn(appSchedulers.computation())
                .filter { !it.isOnError }
                .dematerialize()

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

    private fun getDepositionPartnersFromNetwork(): Observable<DepositionPartnersResponse> {
        return api.getDepositionPartners().toObservable()
                .subscribeOn(appSchedulers.io())
                .materialize()
                .observeOn(appSchedulers.ui())
                .map { notification ->
                    notification.error?.let { sourceError.onNext(it) }
                    notification
                }
                .observeOn(appSchedulers.computation())
                .filter { !it.isOnError }
                .dematerialize<DepositionPartnersResponse>()
    }
}
