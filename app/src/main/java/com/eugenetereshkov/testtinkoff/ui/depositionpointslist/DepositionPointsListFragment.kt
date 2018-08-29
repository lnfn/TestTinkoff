package com.eugenetereshkov.testtinkoff.ui.depositionpointslist

import android.content.Context
import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.ui.global.BaseFragment
import dagger.android.support.AndroidSupportInjection


class DepositionPointsListFragment : BaseFragment() {

    companion object {
        const val TAG = "deposition_points_list_fragment"

        fun newInstance() = DepositionPointsListFragment()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override val idResLayout: Int = R.layout.fragment_deposition_points_list
}
