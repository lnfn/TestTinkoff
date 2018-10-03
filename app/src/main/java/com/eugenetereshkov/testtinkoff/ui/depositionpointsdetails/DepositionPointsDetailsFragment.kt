package com.eugenetereshkov.testtinkoff.ui.depositionpointsdetails

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner
import com.eugenetereshkov.testtinkoff.extension.fromHtml
import com.eugenetereshkov.testtinkoff.extension.getDownloadImageUrl
import com.eugenetereshkov.testtinkoff.extension.loadRoundedImage
import com.eugenetereshkov.testtinkoff.presenter.depositionpointsdetails.DepositionPointDetailsPresenter
import com.eugenetereshkov.testtinkoff.presenter.depositionpointsdetails.DepositionPointDetailsView
import com.eugenetereshkov.testtinkoff.ui.global.BaseFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.deposition_point_details_layout.*
import kotlinx.android.synthetic.main.deposition_point_item.*
import kotlinx.android.synthetic.main.fragment_deposition_points_details.*
import javax.inject.Inject


class DepositionPointsDetailsFragment : BaseFragment(), DepositionPointDetailsView {

    companion object {
        const val TAG = "deposition_points_details_fragment"

        const val DATA = "data"

        fun newInstance(data: DepositionPointAndPartner) = DepositionPointsDetailsFragment().apply {
            arguments = bundleOf(DATA to data)
        }
    }

    override val idResLayout: Int = R.layout.fragment_deposition_points_details

    @Inject
    @InjectPresenter
    lateinit var presenter: DepositionPointDetailsPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private var clickListener: OnClickListener? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.apply {
            title = getString(R.string.deposition_point)
            setNavigationIcon(R.drawable.ic_close_white)
            setNavigationOnClickListener { clickListener?.hideDepositionPointsDetails() }
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        clickListener = when {
            parentFragment is OnClickListener -> parentFragment as OnClickListener
            activity is OnClickListener -> activity as OnClickListener
            else -> null
        }
    }

    override fun onDestroy() {
        clickListener = null
        super.onDestroy()
    }

    override fun showData(data: DepositionPointAndPartner) {
        textViewPartnerName.text = data.name
        textViewAddressInfo.text = data.fullAddress
        textViewDepositionDuration.text = data.depositionDuration.fromHtml()
        textViewLimitations.text = data.limitations.fromHtml()
        textViewDescription.text = data.description.fromHtml()

        data.workHours.isNotBlank().let { visible ->
            textViewWorkHoursLabel.isVisible = visible
            textViewWorkHours.isVisible = visible
            if (visible) textViewWorkHours.text = data.workHours
        }

        imageView.loadRoundedImage(data.picture.getDownloadImageUrl(requireContext()))
    }

    interface OnClickListener {
        fun hideDepositionPointsDetails()
        fun showDepositionPointsDetails(data: DepositionPointAndPartner)
    }
}
