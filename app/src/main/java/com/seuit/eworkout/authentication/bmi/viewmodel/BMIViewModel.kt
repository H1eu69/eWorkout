package com.seuit.eworkout.authentication.bmi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seuit.eworkout.authentication.bmi.model.BMI
import com.seuit.eworkout.authentication.bmi.model.BMIState
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BMIViewModel : ViewModel() {
    //default value
    private val _mainModel : BMI = BMI(null, null, null)
    val mainModel : BMI get() = _mainModel

    private val _skipModel : BMI = BMI(18, 60.0, 165.0)
    val skipModel : BMI get() = _skipModel

    private val bmiLiveData : MutableLiveData<BMI> = MutableLiveData()
    private val _state : MutableLiveData<BMIState> = MutableLiveData()
    val state : LiveData<BMIState> get() = _state

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore

    fun signInIfNotSignInPrevious()
    {
        when(auth.currentUser)
        {
            null -> { //Anonymous account
                bmiLiveData.value = mainModel
                auth.signInAnonymously().addOnSuccessListener {
                    val weight = bmiLiveData.value!!.weight
                    val height = bmiLiveData.value!!.height
                    val data = hashMapOf<String, Any?>(
                        "age" to bmiLiveData.value!!.age,
                        "current_weight" to weight,
                        "current_height" to height,
                    )
                    _state.value = BMIState.SIGNIN_SUCCESS
                    firestore.collection("Users").document(auth.currentUser!!.uid).update(data)


                    val _BMI = (weight!! /((height!!/100)*(height/100)))
                    val data2 = hashMapOf(
                        "user_id" to it.user?.uid,
                        "height" to height,
                        "weight" to weight,
                        "time" to Timestamp.now(),
                        "bmi" to _BMI
                    )
                    firestore.collection("Height_and_Weight").add(data2)
                }
            }
            else -> {
                bmiLiveData.value = mainModel
                val weight = bmiLiveData.value!!.weight
                val height = bmiLiveData.value!!.height

                val data = hashMapOf<String, Any?>(
                    "age" to bmiLiveData.value!!.age,
                    "current_weight" to bmiLiveData.value!!.weight,
                    "current_height" to bmiLiveData.value!!.height,
                )
                _state.value = BMIState.SIGNIN_SUCCESS
                firestore.collection("Users").document(auth.currentUser!!.uid).update(data)

                val _BMI = (weight!! /((height!!/100)*(height/100)))
                val data2 = hashMapOf(
                    "user_id" to auth.currentUser!!.uid,
                    "height" to height,
                    "weight" to weight,
                    "time" to Timestamp.now(),
                    "bmi" to _BMI
                )
                firestore.collection("Height_and_Weight").add(data2)
            }
        }
    }

    fun skipWithDefaultValue()
    {
        val weight = skipModel.weight
        val height = skipModel.height
        val age = skipModel.age
        val data = hashMapOf<String, Any?>(
            "age" to age,
            "current_weight" to weight,
            "current_height" to height,
        )
        firestore.collection("Users").document(auth.currentUser!!.uid).update(data)

        val _BMI = (skipModel.weight!!/((skipModel.height!!/100)*(skipModel.height!!/100)))
        val data2 = hashMapOf(
            "user_id" to auth.currentUser!!.uid,
            "height" to height,
            "weight" to weight,
            "time" to Timestamp.now(),
            "bmi" to _BMI
        )
        firestore.collection("Height_and_Weight").add(data2)
        _state.value = BMIState.SIGNIN_SUCCESS

    }

    fun validateAge(): Boolean {
        if(mainModel.age == null)
        {
            _state.value = BMIState.MISSING_AGE
            return false
        }
        _state.value = BMIState.NO_AGE_ERROR
        return true
    }

    fun validateWeight(): Boolean {
        if(mainModel.weight == null)
        {
            _state.value = BMIState.MISSING_WEIGHT
            return false
        }
        _state.value = BMIState.NO_WEIGHT_ERROR
        return true
    }

    fun validateHeight(): Boolean {
        if(mainModel.height == null)
        {
            _state.value = BMIState.MISSING_HEIGHT
            return false
        }
        _state.value = BMIState.NO_HEIGHT_ERROR
        return true
    }

    fun validateText(): Boolean
    {
        if(validateAge() &&
            validateWeight() &&
            validateHeight()
        ) {
            return true
        }
        return false
    }
}