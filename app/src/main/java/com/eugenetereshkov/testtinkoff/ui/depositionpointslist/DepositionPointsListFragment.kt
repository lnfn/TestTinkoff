package com.eugenetereshkov.testtinkoff.ui.depositionpointslist

import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.ui.global.BaseFragment


class DepositionPointsListFragment : BaseFragment() {

    companion object {
        const val TAG = "deposition_points_list_fragment"

        fun newInstance() = DepositionPointsListFragment()
    }

    override val idResLayout: Int = R.layout.fragment_deposition_points_list
}
