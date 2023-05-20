package com.example.eworkout.report.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.report.model.ReportState
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.util.Calendar

class ReportViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val firestore = Firebase.firestore
    var num: Double = 0.0
    var min: Double = 0.0
    var exercises: Double = 0.0

    val weight_data_list = mutableListOf<Double>()
    var heviest_weight : Double = 0.0
    var lightest_weight : Double = 0.0
    var current_weight : Double = 0.0
    var current_bmi : Double = 0.0
    private var Height_and_Weight_id: String? = null

    private val _state: MutableLiveData<ReportState> = MutableLiveData(ReportState.LOADING)
    val state: LiveData<ReportState> get() = _state


    fun watching(){
        firestore.collection("Calendar")
            .whereEqualTo("user_id", auth.currentUser!!.uid)
            .get().addOnSuccessListener { documents ->
                for(it in documents){
                    val calo = it.get("total_calories") as Double
                    num += calo
                    num = Math.round(num * 100) / 100.0

                    val milliseconds = it.get("total_time") as Double
                    min += milliseconds/60000
                    min = Math.round(min * 100) / 100.0

                    val ex = it.get("number_of_exercise") as Double
                    exercises += ex
                }
                _state.value = ReportState.LOADED
            }.addOnFailureListener {
                Log.d(ContentValues.TAG, "load failed", it)
            }
    }

    fun get_weight_data(){
        firestore.collection("Height_and_Weight")
            .whereEqualTo("user_id", auth.currentUser!!.uid)
            .orderBy("time")
            .get().addOnSuccessListener { documents ->
                for(it in documents){
                    current_bmi = it.get("bmi") as Double
                    val _weight : Double = it.get("weight") as Double
                    weight_data_list.add(_weight)
                }
                if (weight_data_list.isNotEmpty())
                {
                    current_bmi = Math.round(current_bmi * 100) / 100.0
                    current_weight = weight_data_list.last()
                    update_haviest_weight(weight_data_list)
                    update_lightest_weight(weight_data_list)
                }
                else
                {weight_data_list.add(0.0)}

                _state.value = ReportState.LOADED
            }

    }

    fun update_haviest_weight(weight_list : List<Double>){
        for(data in weight_list)
        {
            if(data > heviest_weight)
                heviest_weight = data
        }
    }

    fun update_lightest_weight(weight_list : List<Double>){
        lightest_weight = 1000.0
        for(data in weight_list)
        {
            if(data < lightest_weight)
                lightest_weight = data
        }
    }

    fun update_weight_height(current_weight: Float, current_height: Float) {
        val _BMI: Float = (current_weight/((current_height/100)*(current_height/100)))
        val data = hashMapOf(
            "user_id" to auth.currentUser!!.uid,
            "height" to current_height,
            "weight" to current_weight,
            "time" to Timestamp.now(),
            "bmi" to _BMI
        )
        firestore.collection("Height_and_Weight").add(data).addOnSuccessListener {
            Height_and_Weight_id = it.id
        }
    }

    fun change_state(){
        weight_data_list.clear()
        _state.value = ReportState.LOADING
    }
}