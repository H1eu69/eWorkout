package com.seuit.eworkout.report

import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.seuit.eworkout.report.util.MathRounder

@BindingAdapter("android:progressByDouble")
fun setProgressByDouble(seekBar: SeekBar, progress: Double)
{
    seekBar.progress = progress.toInt()
}

@BindingAdapter("android:textByNumber")
fun setTextByNumber(textView: TextView, number: Double)
{
    textView.text = number.toString()
}

@BindingAdapter("android:textByNumber")
fun setTextByNumber(textView: TextView, number: Int)
{
    textView.text = number.toString()
}

@BindingAdapter("android:textByBMI")
fun setTextByBMI(textView: TextView, BMI: Double)
{
    if(BMI <= 18.5)
        textView.text = "Under Weight"
    if(BMI <= 25 && BMI > 18.5)
        textView.text = "Normal"
    if(BMI in 26.0..30.0)
        textView.text = "Over Weight"
    if(BMI > 30)
        textView.text = "Obesity"
}

@BindingAdapter("android:textWeightByNumber")
fun setTextWeightByNumber(textView: TextView, number: Double)
{
    val text = MathRounder.round(number).toString() + " kg"
    textView.text = text
}