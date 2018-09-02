package com.eugenetereshkov.testtinkoff.ui.depositionpointsdetails

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner
import com.eugenetereshkov.testtinkoff.extension.fromHtml
import com.eugenetereshkov.testtinkoff.extension.getDensityName
import com.eugenetereshkov.testtinkoff.ui.global.BaseFragment
import kotlinx.android.synthetic.main.deposition_point_item.*
import kotlinx.android.synthetic.main.fragment_deposition_points_details.*


class DepositionPointsDetailsFragment : BaseFragment() {

    companion object {
        const val TAG = "deposition_points_details_fragment"

        private const val DATA = "data"

        fun newInstance(data: DepositionPointAndPartner) = DepositionPointsDetailsFragment().apply {
            arguments = bundleOf(DATA to data)
        }
    }

    override val idResLayout: Int = R.layout.fragment_deposition_points_details

    private var clickListener: OnClickListener? = null
    private val data: DepositionPointAndPartner by lazy { arguments?.getParcelable(DATA) as DepositionPointAndPartner }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.apply {
            title = getString(R.string.deposition_point)
            setNavigationIcon(R.drawable.ic_close_white)
            setNavigationOnClickListener { clickListener?.hideDepositionPointsDetails() }
        }

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

        val imageUrl = "${DepositionPointAndPartner.IMAGE_URL}${requireContext().resources.displayMetrics.densityDpi.getDensityName()}/${data.picture}"

        Glide.with(requireContext())
                .asBitmap()
                .load(imageUrl)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        RoundedBitmapDrawableFactory.create(imageView.resources, resource).run {
                            this.isCircular = true
                            imageView.setImageDrawable(this)
                        }
                    }
                })
    }

    override fun onAttach(context: Context?) {
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

    interface OnClickListener {
        fun hideDepositionPointsDetails()
    }
}
