package com.eugenetereshkov.testtinkoff.ui.depositionpointslist

import android.content.Context
import androidx.core.widget.toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.presenter.depositionpointslist.DepositionPointsListPresenter
import com.eugenetereshkov.testtinkoff.presenter.depositionpointslist.DepositionPointsListView
import com.eugenetereshkov.testtinkoff.ui.global.BaseFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class DepositionPointsListFragment : BaseFragment(), DepositionPointsListView {

    companion object {
        const val TAG = "deposition_points_list_fragment"

        fun newInstance() = DepositionPointsListFragment()
    }

    override val idResLayout: Int = R.layout.fragment_deposition_points_list

    @Inject
    @InjectPresenter
    lateinit var presenter: DepositionPointsListPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun showCountItems(size: Int) {
        context?.toast(size.toString())
    }
}
