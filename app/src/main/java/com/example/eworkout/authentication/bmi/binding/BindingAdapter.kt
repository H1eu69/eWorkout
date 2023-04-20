package com.example.eworkout.authentication.bmi.binding

import android.widget.EditText
import androidx.databinding.BindingAdapter


@BindingAdapter("android:setText")
fun setText(view: EditText, text: Int) {
    view.setText(text.toString())
}

@BindingAdapter("android:setText")
fun setText(view: EditText, text: Double) {
    view.setText(text.toString())
}

