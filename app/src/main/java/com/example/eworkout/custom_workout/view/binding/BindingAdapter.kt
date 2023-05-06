package com.example.eworkout.custom_workout.view.binding

import android.content.Context
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("srcByString")
fun setSrcByString(view: ShapeableImageView, imageString: String?) {
    // Important to break potential infinite loops.
    Glide.with(view.context)
        .load(imageString)
        .fitCenter()
        .apply(RequestOptions.bitmapTransform(RoundedCorners(40))) // round
        .into(view);
}

