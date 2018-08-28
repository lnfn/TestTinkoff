package com.eugenetereshkov.testtinkoff.ui.depositionpointsmap

import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.ui.global.BaseFragment


class DepositionPointsMapFragment : BaseFragment() {

    companion object {
        const val TAG = "deposition_points_map_fragment"

        fun newInstance() = DepositionPointsMapFragment()
    }

    override val idResLayout: Int = R.layout.fragment_deposition_points_map
}
