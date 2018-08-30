package com.eugenetereshkov.testtinkoff.entity

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


class DepositionPointClusterItem(
        val latitude: Double,
        val longitude: Double,
        val name: String
) : ClusterItem {

    override fun getSnippet(): String = name

    override fun getTitle(): String = name

    override fun getPosition(): LatLng = LatLng(latitude, longitude)
}
