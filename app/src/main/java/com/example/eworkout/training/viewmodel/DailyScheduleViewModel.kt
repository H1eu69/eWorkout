package com.example.eworkout.training.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.training.model.ScheduleState
import com.example.eworkout.training.model.TrainingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.util.*

class DailyScheduleViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val firestore = Firebase.firestore
    val calendar = Calendar.getInstance().time
    val dateFormat = DateFormat.getDateInstance().format(calendar)
    var num: Double = 0.0
    var min: Double = 0.0
    var exercises: Double = 0.0
    private val _state: MutableLiveData<ScheduleState> = MutableLiveData(ScheduleState.LOADING)
    val state: LiveData<ScheduleState> get() = _state


    fun watching(){
        firestore.collection("Calendar")
            .whereEqualTo("user_id", auth.currentUser!!.uid)
            .whereEqualTo("date", dateFormat)
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

                    _state.value = ScheduleState.LOADED
                }
            }.addOnFailureListener {
                Log.d(ContentValues.TAG, "load failed", it)
            }
    }
}