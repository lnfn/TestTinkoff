package com.eugenetereshkov.testtinkoff.presenter.depositionpointsmap

import android.location.Location
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner


@StateStrategyType(AddToEndSingleStrategy::class)
interface DepositionPointsMapView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openGpsSettings()

    fun showGpsError()
    fun toggleLocationUpdating(updating: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showLocationDelay()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun moveCameraToUserLocation(location: Location, zoom: Float? = null)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideLocationDelay()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showLastLocation(location: Location)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMarkers(markers: List<DepositionPointAndPartner>)
}
