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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class ReportViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val firestore = Firebase.firestore
    var num: Double = 0.0
    var min: Double = 0.0
    var exercises: Double = 0.0

    val point_list = mutableListOf<Double>()
    val average_list = mutableListOf<Double>()
    val weight_data_list = mutableListOf<Double>()
    var heviest_weight : Double = 0.0
    var lightest_weight : Double = 0.0
    var current_weight : Double = 0.0
    var current_bmi : Double = 0.0
    var _point1 : Double = 0.0
    var _point2 : Double = 0.0
    var _point3 : Double = 0.0
    var _point4 : Double = 0.0
    var _point5 : Double = 0.0
    var _point6 : Double = 0.0
    var _point7 : Double = 0.0
    var _point8 : Double = 0.0
    var _point9 : Double = 0.0
    var _point10 : Double = 0.0
    var _point11 : Double = 0.0
    var _point12 : Double = 0.0
    private var Height_and_Weight_id: String? = null
    var month4: String? = null
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

                    val _month = it.getDate("time")
                    val month = _month.toString().subSequence(4,7)

                    weight_data_list.add(_weight)

                    when(month){
                        "Jan" -> {
                            average_list.add(_weight)
                            _point1 = average_list.average()
                        }
                        "Feb" -> {
                            average_list.add(_weight)
                            _point2 = average_list.average()
                        }
                        "Mar" -> {
                            average_list.add(_weight)
                            _point3 = average_list.average()
                        }
                        "Apr" -> {
                            average_list.add(_weight)
                            _point4 = average_list.average()
                        }
                        "May" -> {
                            average_list.add(_weight)
                            _point5 = average_list.average()
                        }
                        "Jun" -> {
                            average_list.add(_weight)
                            _point6 = average_list.average()
                        }
                        "Jul" -> {
                            average_list.add(_weight)
                            _point7 = average_list.average()
                        }
                        "Aug" -> {
                            average_list.add(_weight)
                            _point5 = average_list.average()
                        }
                        "Sep" -> {
                            average_list.add(_weight)
                            _point6 = average_list.average()
                        }
                        "Oct" -> {
                            average_list.add(_weight)
                            _point5 = average_list.average()
                        }
                        "Nov" -> {
                            average_list.add(_weight)
                            _point5 = average_list.average()
                        }
                        "Dec" -> {
                            average_list.add(_weight)
                            _point5 = average_list.average()
                        }
                    }

                }
                point_list.add(_point1)
                point_list.add(_point2)
                point_list.add(_point3)
                point_list.add(_point4)
                point_list.add(_point5)
                point_list.add(_point6)
                point_list.add(_point7)
                point_list.add(_point8)
                point_list.add(_point9)
                point_list.add(_point10)
                point_list.add(_point11)
                point_list.add(_point12)
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