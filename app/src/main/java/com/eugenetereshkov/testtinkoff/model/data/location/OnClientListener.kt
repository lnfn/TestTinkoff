package com.eugenetereshkov.testtinkoff.model.data.location

import android.location.Location


interface OnClientListener {
    fun updateUserLocation(location: Location)

    fun onGpsError()

    fun onUpdateLocationError()
}
