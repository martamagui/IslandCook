package com.marta.islandcook.utils

import android.widget.ImageView
import com.marta.islandcook.R
import com.squareup.picasso.Picasso

fun ImageView.imageUrl(imageUrl: String?) {
    if(imageUrl!=null){
        Picasso.get().load(imageUrl)
            .placeholder(R.drawable.gradient_placeholder)
            .error(R.drawable.default_img)
            .into(this)
    }
}