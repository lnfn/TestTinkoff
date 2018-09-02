package com.eugenetereshkov.testtinkoff.ui.depositionpointslist

import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner
import com.eugenetereshkov.testtinkoff.extension.getDensityName
import com.eugenetereshkov.testtinkoff.extension.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.deposition_point_item.*


class DepositionPointsAdapter : ListAdapter<DepositionPointAndPartner, DepositionPointsAdapter.DepositionPointViewHolder>(DiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepositionPointViewHolder {
        return DepositionPointViewHolder(parent.inflate(R.layout.deposition_point_item))
    }

    override fun onBindViewHolder(viewHolder: DepositionPointViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    class DepositionPointViewHolder(
            override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: DepositionPointAndPartner) {
            textViewPartnerName.text = item.name
            textViewAddressInfo.text = item.fullAddress

            val imageUrl = "${DepositionPointAndPartner.IMAGE_URL}${itemView.context.resources.displayMetrics.densityDpi.getDensityName()}/${item.picture}"

            Glide.with(itemView.context)
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
    }

    object DiffItemCallback : DiffUtil.ItemCallback<DepositionPointAndPartner>() {
        override fun areItemsTheSame(p0: DepositionPointAndPartner, p1: DepositionPointAndPartner): Boolean = p0.id == p1.id
        override fun areContentsTheSame(p0: DepositionPointAndPartner, p1: DepositionPointAndPartner): Boolean = p0 === p1
    }
}
