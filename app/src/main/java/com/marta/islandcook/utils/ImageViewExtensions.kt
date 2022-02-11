package com.marta.islandcook.utils

import android.widget.ImageView
import com.marta.islandcook.R
import com.squareup.picasso.Picasso

fun ImageView.imageUrl(imageUrl: String) {
    Picasso.get().load(imageUrl)
        .placeholder(R.drawable.default_img)
        .into(this)
}