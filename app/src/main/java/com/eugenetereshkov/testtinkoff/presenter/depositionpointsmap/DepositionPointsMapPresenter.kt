package com.eugenetereshkov.testtinkoff.presenter.depositionpointsmap

import android.location.Location
import android.location.LocationManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.eugenetereshkov.testtinkoff.extension.bindTo
import com.eugenetereshkov.testtinkoff.model.data.location.LocationProvider
import com.eugenetereshkov.testtinkoff.model.data.location.OnClientListener
import com.eugenetereshkov.testtinkoff.model.data.location.OnLocationUpdatingListener
import com.eugenetereshkov.testtinkoff.model.repository.IDepositionPointsRepository
import com.eugenetereshkov.testtinkoff.ui.depositionpointsmap.DepositionPointsMapFragment
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject


@InjectViewState
class DepositionPointsMapPresenter @Inject constructor(
        private val locationProvider: LocationProvider,
        private val depositionPointsRepository: IDepositionPointsRepository
) : MvpPresenter<DepositionPointsMapView>(), OnLocationUpdatingListener {

    private var isFirstLocationRequest = true
    private var isSecondNeverUsedPermission: Boolean = false
    private var location: Location = Location(LocationManager.GPS_PROVIDER).apply {
        latitude = 55.751244
        longitude = 37.618423
    }
    private val disposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        depositionPointsRepository.source
                .subscribe(
                        { viewState.showMarkers(it) },
                        { Timber.e(it) }
                )
                .bindTo(disposable)
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    override fun setComplete(isUpdating: Boolean) {

    }

    fun getDepositionPoints(latitude: Double, longitude: Double, mapVisibleRadius: Int) {
        depositionPointsRepository.request.onNext(
                DepositionPointsMapFragment.TargetMapPosition(latitude = latitude,
                        longitude = longitude,
                        radius = mapVisibleRadius

                )
        )
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
                DepositionPointsMapFragment.DEFAULT_ZOOM
        )

        depositionPointsRepository.source
                .subscribe(
                        { Timber.d(it.toString()) },
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
