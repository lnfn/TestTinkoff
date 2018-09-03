package com.eugenetereshkov.testtinkoff.extension

import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition


fun ImageView.loadRoundedImage(url: String) {
    Glide.with(this.context)
            .asBitmap()
            .load(url)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    RoundedBitmapDrawableFactory.create(this@loadRoundedImage.resources, resource).run {
                        this.isCircular = true
                        this@loadRoundedImage.setImageDrawable(this)
                    }
                }
            })
}
