package com.example.eworkout.training.view.binding

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.example.eworkout.training.model.Exercise
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textfield.TextInputEditText

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

@BindingAdapter("textByNumber")
fun setTextByNumber(view: TextView, text: Double?) {
    if(text != null)
        view.setText(text.toString())
    else
        view.text = null}

@InverseBindingAdapter(attribute = "textByNumber")
fun getTextByNumber2(view: TextView) : Double?{
    return if(view.text.toString() != "") view.text.toString().toDouble()
    else null
}

@BindingAdapter("textByNumberAttrChanged")
fun setListeners(
    view: TextView,
    attrChange: InverseBindingListener
) {
    view.addTextChangedListener (object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            attrChange.onChange()
        }
    }
    )
}

