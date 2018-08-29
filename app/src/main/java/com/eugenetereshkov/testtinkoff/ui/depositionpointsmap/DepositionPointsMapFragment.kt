package com.eugenetereshkov.testtinkoff.ui.depositionpointsmap

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.extension.showSettingsRequest
import com.eugenetereshkov.testtinkoff.presenter.depositionpointscontainer.DepositionPointsContainerPresenter
import com.eugenetereshkov.testtinkoff.presenter.depositionpointscontainer.DepositionPointsContainerView
import com.eugenetereshkov.testtinkoff.presenter.depositionpointsmap.DepositionPointsMapPresenter
import com.eugenetereshkov.testtinkoff.presenter.depositionpointsmap.DepositionPointsMapView
import com.eugenetereshkov.testtinkoff.ui.global.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_deposition_points_map.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import javax.inject.Inject


@RuntimePermissions
class DepositionPointsMapFragment : BaseFragment(), OnMapReadyCallback, DepositionPointsMapView,
        DepositionPointsContainerView {

    companion object {
        const val TAG = "deposition_points_map_fragment"
        const val REQUEST_CHECK_SETTINGS = 228
        const val DEFAULT_ZOOM = 10f

        fun newInstance() = DepositionPointsMapFragment()
    }

    override val idResLayout: Int = R.layout.fragment_deposition_points_map

    @Inject
    @InjectPresenter
    lateinit var presenter: DepositionPointsContainerPresenter
    @InjectPresenter
    @Inject
    lateinit var mapPresenter: DepositionPointsMapPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    @ProvidePresenter
    fun providerMapPresenter() = mapPresenter

    private lateinit var googleMap: GoogleMap
    private val locationDelaySnackbar by lazy {
        Snackbar.make(root, getText(R.string.update_location), Snackbar.LENGTH_INDEFINITE)
    }
    private val onClickListener by lazy {
        View.OnClickListener {
            when (it.id) {
                imageButtonFindLocation.id -> findCurrentLocationWithPermissionCheck()
                imageButtonPlusZoom.id -> googleMap.animateCamera(CameraUpdateFactory.zoomIn())
                imageButtonMinusZoom.id -> googleMap.animateCamera(CameraUpdateFactory.zoomOut())
            }
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        imageButtonFindLocation.setOnClickListener(onClickListener)
        imageButtonPlusZoom.setOnClickListener(onClickListener)
        imageButtonMinusZoom.setOnClickListener(onClickListener)
    }

    override fun onStart() {
        super.onStart()

        mapPresenter.onStart()
    }

    override fun onResume() {
        super.onResume()

        mapPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()

        mapPresenter.onPause()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        mapPresenter.onMapReady()
    }

    override fun showLastLocation(location: Location) {
        googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        LatLng(location.latitude, location.longitude), DEFAULT_ZOOM
                )
        )
    }

    override fun openGpsSettings() {
        showSettingsRequest(R.string.open_Settings)
    }

    override fun showGpsError() {
    }

    override fun toggleLocationUpdating(updating: Boolean) {

    }

    override fun showLocationDelay() {
        locationDelaySnackbar.show()
    }

    override fun hideLocationDelay() {
        if (locationDelaySnackbar.isShown) locationDelaySnackbar.dismiss()
    }

    override fun moveCameraToUserLocation(location: Location, zoom: Float?) {
        val latLng = LatLng(location.latitude, location.longitude)
        val cameraUpdate = zoom?.let { CameraUpdateFactory.newLatLngZoom(latLng, it) }
                ?: CameraUpdateFactory.newLatLng(latLng)
        googleMap.animateCamera(cameraUpdate)
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun findCurrentLocation() {
        mapPresenter.setPermissionsGranted()
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onDeniedLocationPermission() {
        mapPresenter.deniedLocationPermission()
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onNeverUsedLocationPermission() {
        mapPresenter.neverUsedLocationPermission()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> mapPresenter.setPermissionsGranted()
                Activity.RESULT_CANCELED -> mapPresenter.onGpsPermissionError()
            }
        }
    }
}
