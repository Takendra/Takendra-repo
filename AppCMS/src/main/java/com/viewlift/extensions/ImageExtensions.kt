package com.viewlift.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.viewlift.R


fun ImageView.loadUrl(imageUrl: String?, placeholder: Int = R.drawable.vid_image_placeholder_port) {
    Glide.with(context)
            .load(imageUrl ?: "")
            .apply(RequestOptions().apply {
                diskCacheStrategy(DiskCacheStrategy.ALL)
                placeholder(placeholder)
            }).into(this)
}