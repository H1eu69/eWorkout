package com.example.eworkout.training.view.binding

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.eworkout.training.model.Exercise
import com.facebook.shimmer.ShimmerFrameLayout

@BindingAdapter("android:isGone")
fun isGone(view: ShimmerFrameLayout, isGone: Boolean) {
    if(isGone)
        view.visibility = View.GONE
    else
        view.visibility = View.VISIBLE
}

@BindingAdapter("android:isGone")
fun isGone(view: ConstraintLayout, isGone: Boolean) {
    if(isGone)
        view.visibility = View.GONE
    else
        view.visibility = View.VISIBLE
}

