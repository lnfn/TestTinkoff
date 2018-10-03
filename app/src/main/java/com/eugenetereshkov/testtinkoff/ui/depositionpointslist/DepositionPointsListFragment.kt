package com.eugenetereshkov.testtinkoff.ui.depositionpointslist

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import androidx.core.widget.toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner
import com.eugenetereshkov.testtinkoff.presenter.depositionpointslist.DepositionPointsListPresenter
import com.eugenetereshkov.testtinkoff.presenter.depositionpointslist.DepositionPointsListView
import com.eugenetereshkov.testtinkoff.ui.depositionpointsdetails.DepositionPointsDetailsFragment
import com.eugenetereshkov.testtinkoff.ui.global.BaseFragment
import com.eugenetereshkov.testtinkoff.ui.global.SimpleDividerDecorator
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_deposition_points_list.*
import javax.inject.Inject


class DepositionPointsListFragment : BaseFragment(), DepositionPointsListView,
        DepositionPointsAdapter.OnItemSelectListener {

    companion object {
        const val TAG = "deposition_points_list_fragment"

        fun newInstance() = DepositionPointsListFragment()
    }

    override val idResLayout: Int = R.layout.fragment_deposition_points_list

    @Inject
    @InjectPresenter
    lateinit var presenter: DepositionPointsListPresenter

    private val adapter by lazy { DepositionPointsAdapter(this) }
    private var clickListener: DepositionPointsDetailsFragment.OnClickListener? = null

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        clickListener = when {
            parentFragment is DepositionPointsDetailsFragment.OnClickListener -> parentFragment as DepositionPointsDetailsFragment.OnClickListener
            activity is DepositionPointsDetailsFragment.OnClickListener -> activity as DepositionPointsDetailsFragment.OnClickListener
            else -> null
        }
    }

    override fun onDetach() {
        clickListener = null
        super.onDetach()
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

    override fun onSelect(data: DepositionPointAndPartner) {
        clickListener?.showDepositionPointsDetails(data)
    }

    override fun showError(error: String) {
        context?.toast(error)
    }
}
