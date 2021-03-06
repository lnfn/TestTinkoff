package com.eugenetereshkov.testtinkoff.presenter.depositionpointsmap

import android.location.Location
import android.location.LocationManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner
import com.eugenetereshkov.testtinkoff.entity.DepositionPointClusterItem
import com.eugenetereshkov.testtinkoff.entity.TargetMapPosition
import com.eugenetereshkov.testtinkoff.extension.bindTo
import com.eugenetereshkov.testtinkoff.model.data.location.LocationProvider
import com.eugenetereshkov.testtinkoff.model.data.location.OnClientListener
import com.eugenetereshkov.testtinkoff.model.data.location.OnLocationUpdatingListener
import com.eugenetereshkov.testtinkoff.model.repository.IDepositionPointsRepository
import com.eugenetereshkov.testtinkoff.model.system.AppPreferences
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject


@InjectViewState
class DepositionPointsMapPresenter @Inject constructor(
        private val locationProvider: LocationProvider,
        private val depositionPointsRepository: IDepositionPointsRepository,
        private val appPreferences: AppPreferences
) : MvpPresenter<DepositionPointsMapView>(), OnLocationUpdatingListener {

    private var isFirstLocationRequest = true
    private var isSecondNeverUsedPermission: Boolean = false
    private var location: Location = Location(LocationManager.GPS_PROVIDER).apply {
        latitude = appPreferences.lastMapPosition.latitude
        longitude = appPreferences.lastMapPosition.longitude
    }
    private val disposable = CompositeDisposable()
    lateinit var lastSelectItem: DepositionPointAndPartner

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    override fun setComplete(isUpdating: Boolean) {

    }

    fun getDepositionPoints(targetMapPosition: TargetMapPosition) {
        depositionPointsRepository.request.onNext(targetMapPosition)
    }

    fun onStart() {
        initLocationProvider()
    }

    fun onResume() {
        locationProvider.startLocationUpdates(this)
    }

    fun onPause() {
        locationProvider.stopLocationUpdates(this)
    }

    fun onMapReady() {
        viewState.moveCameraToUserLocation(
                location,
                appPreferences.lastMapPosition.zoom
        )

        depositionPointsRepository.sourceObservable
                .subscribe(
                        { viewState.showMarkers(it) },
                        { Timber.e(it) }
                )
                .bindTo(disposable)
    }

    fun onGpsPermissionError() {
        locationProvider.isPermissionsGranted = false
        viewState.hideLocationDelay()
    }

    fun setPermissionsGranted() {
        locationProvider.isPermissionsGranted = true
        locationProvider.updateLastUserLocation()
        locationProvider.startLocationUpdates(this)

        if (!isFirstLocationRequest) {
            viewState.moveCameraToUserLocation(location)
        }
    }

    fun deniedLocationPermission() {
        viewState.showGpsError()
    }

    fun neverUsedLocationPermission() {
        if (isSecondNeverUsedPermission) {
            viewState.openGpsSettings()
        } else {
            isSecondNeverUsedPermission = true
        }
    }

    private fun convertToClusterItem(item: DepositionPointAndPartner) = DepositionPointClusterItem(
            latitude = item.location.latitude,
            longitude = item.location.longitude,
            name = item.name
    )

    private fun initLocationProvider() {
        locationProvider.setOnUpdateUserLocationListener(object : OnClientListener {
            override fun updateUserLocation(location: Location) {
                onUpdatedUserLocation(location)
            }

            override fun onGpsError() {
                onGpsPermissionError()
            }

            override fun onUpdateLocationError() {
                viewState.showLocationDelay()
            }
        })
    }

    private fun onUpdatedUserLocation(location: Location) {
        viewState.hideLocationDelay()

        if (isFirstLocationRequest) {
            isFirstLocationRequest = false
            viewState.moveCameraToUserLocation(location)
        }

        this@DepositionPointsMapPresenter.location = location
    }
}
