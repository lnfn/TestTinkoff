package com.eugenetereshkov.testtinkoff.extension

import android.location.Location
import com.google.android.gms.maps.GoogleMap


fun GoogleMap.getMapVisibleRadius(): Double {
    val visibleRegion = this.projection.visibleRegion
    val distanceWidth = FloatArray(1)
    val distanceHeight = FloatArray(1)

    val farRight = visibleRegion.farRight
    val farLeft = visibleRegion.farLeft
    val nearRight = visibleRegion.nearRight
    val nearLeft = visibleRegion.nearLeft

    Location.distanceBetween(
            (farLeft.latitude + nearLeft.latitude) / 2,
            farLeft.longitude,
            (farRight.latitude + nearRight.latitude) / 2,
            farRight.longitude,
            distanceWidth
    )

    Location.distanceBetween(
            (farLeft.latitude + nearLeft.latitude) / 2,
            farLeft.longitude,
            (farRight.latitude + nearRight.latitude) / 2,
            farRight.longitude,
            distanceWidth
    )

    Location.distanceBetween(
            farRight.latitude,
            (farRight.longitude + farLeft.longitude) / 2,
            nearRight.latitude,
            (nearRight.longitude + nearLeft.longitude) / 2,
            distanceHeight
    )

    val radiusInMeters = Math.sqrt(
            Math.pow(distanceWidth[0].toDouble(), 2.0) + Math.pow(distanceHeight[0].toDouble(), 2.0)
    ) / 2
    return radiusInMeters
}
