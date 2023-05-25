package com.example.eworkout.detection

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.MutableLiveData


@BindingAdapter("textByReps")
fun setTextByReps(view: TextView, text: MutableLiveData<Int>?) {
    Log.d("text", text.toString())
    Log.d("text value", text?.value.toString())
    if(text != null){
        if(text.value!! > 1)
            view.text = "You have done ${text.value} reps"
        else
            view.text = "You have done ${text.value} rep"
    }
    else
        view.text = null
}

@InverseBindingAdapter(attribute = "textByReps")
fun getTextByReps(view: TextView) : Int?{
    return if(view.text.toString().contains("You have done ")
            || view.text.toString().contains(" reps")
            || view.text.toString().contains(" rep"))
        view.text.toString()
            .replace("You have done ", "")
            .replace(" reps","")
            .replace(" rep","")
            .toInt()
    else null
}

@BindingAdapter("textByRepsAttrChanged")
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

