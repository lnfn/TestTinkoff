package com.eugenetereshkov.testtinkoff.ui.depositionpointslist

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner
import com.eugenetereshkov.testtinkoff.presenter.depositionpointslist.DepositionPointsListPresenter
import com.eugenetereshkov.testtinkoff.presenter.depositionpointslist.DepositionPointsListView
import com.eugenetereshkov.testtinkoff.ui.global.BaseFragment
import com.eugenetereshkov.testtinkoff.ui.global.SimpleDividerDecorator
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_deposition_points_list.*
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

    private val adapter by lazy { DepositionPointsAdapter() }

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = this@DepositionPointsListFragment.adapter
            addItemDecoration(SimpleDividerDecorator(requireContext()))
        }
    }

    override fun showCountItems(data: List<DepositionPointAndPartner>) {
        adapter.submitList(data)
    }
}
