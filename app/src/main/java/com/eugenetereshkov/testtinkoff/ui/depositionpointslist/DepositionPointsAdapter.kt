package com.eugenetereshkov.testtinkoff.ui.depositionpointslist

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner
import com.eugenetereshkov.testtinkoff.extension.getDownloadImageUrl
import com.eugenetereshkov.testtinkoff.extension.inflate
import com.eugenetereshkov.testtinkoff.extension.loadRoundedImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.deposition_point_item.*


class DepositionPointsAdapter(
        private val onItemSelectListener: OnItemSelectListener
) : ListAdapter<DepositionPointAndPartner, DepositionPointsAdapter.DepositionPointViewHolder>(DiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepositionPointViewHolder {
        return DepositionPointViewHolder(parent.inflate(R.layout.deposition_point_item), { onItemSelectListener.onSelect(it) })
    }

    override fun onBindViewHolder(viewHolder: DepositionPointViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    class DepositionPointViewHolder(
            override val containerView: View,
            private val selectListener: (DepositionPointAndPartner) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private lateinit var item: DepositionPointAndPartner

        init {
            itemView.setOnClickListener { selectListener(this.item) }
        }

        fun bind(item: DepositionPointAndPartner) {
            this.item = item

            textViewPartnerName.text = item.name

            textViewAddressInfo.apply {
                maxLines = 1
                text = item.fullAddress
            }

            imageView.loadRoundedImage(item.picture.getDownloadImageUrl(itemView.context))
        }
    }

    object DiffItemCallback : DiffUtil.ItemCallback<DepositionPointAndPartner>() {
        override fun areItemsTheSame(p0: DepositionPointAndPartner, p1: DepositionPointAndPartner): Boolean = p0.id == p1.id
        override fun areContentsTheSame(p0: DepositionPointAndPartner, p1: DepositionPointAndPartner): Boolean = p0 === p1
    }

    interface OnItemSelectListener {
        fun onSelect(data: DepositionPointAndPartner)
    }
}
