package com.seuit.eworkout.authentication.bmi.binding

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.*
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText


@BindingAdapter("textByNumber")
fun setTextByNumber(view: TextInputEditText, text: Int?) {
    if(text != null)
        view.setText(text.toString())
    else
        view.text = null
}

@BindingAdapter("textByNumber")
fun setTextByNumber(view: TextInputEditText, text: Double?) {
    if(text != null)
        view.setText(text.toString())
    else
        view.text = null}

@InverseBindingAdapter(attribute = "textByNumber")
fun getTextByNumber(view: TextInputEditText) : Int? {
    return if(view.text.toString() != "") view.text.toString().toInt()
    else null
}
@InverseBindingAdapter(attribute = "textByNumber")
fun getTextByNumber2(view: TextInputEditText) : Double?{
    return if(view.text.toString() != "") view.text.toString().toDouble()
    else null
}

@BindingAdapter("textByNumberAttrChanged")
fun setListeners(
    view: TextInputEditText,
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
