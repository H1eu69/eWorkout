package com.example.eworkout.authentication.bmi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.authentication.bmi.model.BMI

class BMIViewModel : ViewModel() {
    private val model : BMI = BMI(18, 60.0, 165.0)

    private val _bmiLiveData : MutableLiveData<BMI> = MutableLiveData(model)
    val bmiLiveData : LiveData<BMI> get() = _bmiLiveData


}