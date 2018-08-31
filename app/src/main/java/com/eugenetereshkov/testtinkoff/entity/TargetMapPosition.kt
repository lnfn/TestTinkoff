package com.eugenetereshkov.testtinkoff.entity


data class TargetMapPosition(
        val latitude: Double,
        val longitude: Double,
        val radius: Int,
        val zoom: Float
) {
    companion object {
        val DEFAULT_TARGET_POSITION = TargetMapPosition(
                latitude = 55.751244,
                longitude = 37.618423,
                radius = 1000,
                zoom = 12f
        )
    }
}
