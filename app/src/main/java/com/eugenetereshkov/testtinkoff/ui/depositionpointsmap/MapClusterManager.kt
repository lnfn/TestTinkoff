package com.eugenetereshkov.testtinkoff.ui.depositionpointsmap

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager


class MapClusterManager<T : ClusterItem>(
        private val context: Context,
        private val googleMap: GoogleMap,
        private val cameraIdleListener: () -> Unit
) : ClusterManager<T>(context, googleMap) {

    override fun onCameraIdle() {
        super.onCameraIdle()
        cameraIdleListener()
    }
}
