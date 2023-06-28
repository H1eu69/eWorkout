package com.seuit.eworkout.custom_workout.view.binding

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("srcByString")
fun setSrcByString(view: ShapeableImageView, imageString: String?) {
    // Important to break potential infinite loops.
    var requestOption = RequestOptions()
    requestOption = requestOption.transform(CenterCrop(), RoundedCorners(40))

    Glide.with(view.context)
        .load(imageString)
        .apply(requestOption)
        .into(view);
}

