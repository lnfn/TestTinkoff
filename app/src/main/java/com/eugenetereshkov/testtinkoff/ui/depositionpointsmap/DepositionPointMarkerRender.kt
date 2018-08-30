package com.eugenetereshkov.testtinkoff.ui.depositionpointsmap

import android.content.Context
import com.eugenetereshkov.testtinkoff.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer


class DepositionPointMarkerRender<T : ClusterItem>(
        context: Context,
        googleMap: GoogleMap,
        clusterManager: ClusterManager<T>
) : DefaultClusterRenderer<T>(
        context,
        googleMap,
        clusterManager
) {

    override fun onBeforeClusterItemRendered(item: T, markerOptions: MarkerOptions) {
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_marker))
    }

    override fun shouldRenderAsCluster(cluster: Cluster<T>): Boolean {
        return cluster.size > 2
    }
}
