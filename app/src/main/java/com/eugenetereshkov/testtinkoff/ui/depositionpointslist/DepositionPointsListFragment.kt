package com.eugenetereshkov.testtinkoff.ui.depositionpointslist

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.presenter.depositionpointscontainer.DepositionPointsContainerPresenter
import com.eugenetereshkov.testtinkoff.ui.global.BaseFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class DepositionPointsListFragment : BaseFragment() {

    companion object {
        const val TAG = "deposition_points_list_fragment"

        fun newInstance() = DepositionPointsListFragment()
    }

    @Inject
    lateinit var presenter: DepositionPointsContainerPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Toast.makeText(
                requireContext(),
                "DepositionPointsListFragment ${presenter.value}",
                Toast.LENGTH_SHORT
        ).show()
    }

    override val idResLayout: Int = R.layout.fragment_deposition_points_list
}
