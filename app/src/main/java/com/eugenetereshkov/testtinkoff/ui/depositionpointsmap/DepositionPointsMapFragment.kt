package com.eugenetereshkov.testtinkoff.ui.depositionpointsmap

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.FrameLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner
import com.eugenetereshkov.testtinkoff.entity.TargetMapPosition
import com.eugenetereshkov.testtinkoff.extension.getDownloadImageUrl
import com.eugenetereshkov.testtinkoff.extension.getMapVisibleRadius
import com.eugenetereshkov.testtinkoff.extension.loadRoundedImage
import com.eugenetereshkov.testtinkoff.extension.showSettingsRequest
import com.eugenetereshkov.testtinkoff.presenter.depositionpointsmap.DepositionPointsMapPresenter
import com.eugenetereshkov.testtinkoff.presenter.depositionpointsmap.DepositionPointsMapView
import com.eugenetereshkov.testtinkoff.ui.depositionpointsdetails.DepositionPointsDetailsFragment
import com.eugenetereshkov.testtinkoff.ui.global.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.deposition_point_item.*
import kotlinx.android.synthetic.main.fragment_deposition_points_map.*
import kotlinx.android.synthetic.main.widget_map_bottomsheet.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import javax.inject.Inject


@RuntimePermissions
class DepositionPointsMapFragment : BaseFragment(), OnMapReadyCallback, DepositionPointsMapView {

    companion object {
        const val TAG = "deposition_points_map_fragment"
        const val REQUEST_CHECK_SETTINGS = 228
        const val DEFAULT_ZOOM = 10f

        fun newInstance() = DepositionPointsMapFragment()
    }

    override val idResLayout: Int = R.layout.fragment_deposition_points_map

    @InjectPresenter
    @Inject
    lateinit var presenter: DepositionPointsMapPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private lateinit var googleMap: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private var clickListener: DepositionPointsDetailsFragment.OnClickListener? = null
    private val cameraIdleListener: () -> Unit = {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        val currentLatLang = googleMap.cameraPosition.target
        val targetMapPosition = TargetMapPosition(
                latitude = currentLatLang.latitude,
                longitude = currentLatLang.longitude,
                radius = googleMap.getMapVisibleRadius().toInt(),
                zoom = googleMap.cameraPosition.zoom

        )
        presenter.getDepositionPoints(targetMapPosition)
    }
    private val clusterClickListener by lazy {
        ClusterManager.OnClusterClickListener<DepositionPointAndPartner> { cluster ->
            val currentZoom = googleMap.cameraPosition.zoom

            if (currentZoom < googleMap.maxZoomLevel) {
                val zoom = currentZoom + 1f
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cluster.position, zoom))
            }
            true
        }
    }
    private val clusterItemClickListener by lazy {
        ClusterManager.OnClusterItemClickListener<DepositionPointAndPartner> { clusterItem ->
            presenter.lastSelectItem = clusterItem
            bottomSheetBehavior.peekHeight = bottomSheet.top
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            textViewPartnerName.text = clusterItem.name

            textViewAddressInfo.apply {
                maxLines = 1
                text = clusterItem.fullAddress
            }

            imageView.loadRoundedImage(clusterItem.picture.getDownloadImageUrl(requireContext()))
            true
        }
    }
    private val clusterManager by lazy {
        MapClusterManager<DepositionPointAndPartner>(requireContext(), googleMap, cameraIdleListener).apply {
            renderer = DepositionPointMarkerRender(requireContext(), googleMap, this)
            setOnClusterClickListener(clusterClickListener)
            setOnClusterItemClickListener(clusterItemClickListener)
        }
    }
    private val locationDelaySnackbar by lazy {
        Snackbar.make(root, getText(R.string.update_location), Snackbar.LENGTH_INDEFINITE)
    }
    private val onClickListener by lazy {
        View.OnClickListener {
            when (it.id) {
                imageButtonFindLocation.id -> findCurrentLocationWithPermissionCheck()
                imageButtonPlusZoom.id -> googleMap.animateCamera(CameraUpdateFactory.zoomIn())
                imageButtonMinusZoom.id -> googleMap.animateCamera(CameraUpdateFactory.zoomOut())
                bottomSheet.id -> clickListener?.showDepositionPointsDetails(presenter.lastSelectItem)
            }
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        clickListener = when {
            parentFragment is DepositionPointsDetailsFragment.OnClickListener -> parentFragment as DepositionPointsDetailsFragment.OnClickListener
            activity is DepositionPointsDetailsFragment.OnClickListener -> activity as DepositionPointsDetailsFragment.OnClickListener
            else -> null
        }
    }

    override fun onDetach() {
        clickListener = null
        super.onDetach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        imageButtonFindLocation.setOnClickListener(onClickListener)
        imageButtonPlusZoom.setOnClickListener(onClickListener)
        imageButtonMinusZoom.setOnClickListener(onClickListener)
        bottomSheet.setOnClickListener(onClickListener)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

    }

    override fun onStart() {
        super.onStart()

        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()

        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()

        presenter.onPause()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        presenter.onMapReady()

        googleMap.apply {
            setOnMarkerClickListener(clusterManager)
            setOnCameraIdleListener(clusterManager)
        }
    }

    override fun showLastLocation(location: Location) {
        googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        LatLng(location.latitude, location.longitude), DEFAULT_ZOOM
                )
        )
    }

    override fun showMarkers(data: List<DepositionPointAndPartner>) {
        if (::googleMap.isInitialized) {
            clusterManager.clearItems()
            clusterManager.addItems(data)
            clusterManager.cluster()
        }
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
        presenter.setPermissionsGranted()
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onDeniedLocationPermission() {
        presenter.deniedLocationPermission()
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onNeverUsedLocationPermission() {
        presenter.neverUsedLocationPermission()
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
                Activity.RESULT_OK -> presenter.setPermissionsGranted()
                Activity.RESULT_CANCELED -> presenter.onGpsPermissionError()
            }
        }
    }
}
