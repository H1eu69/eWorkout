package com.example.eworkout.training.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.training.model.HistoryState
import com.example.eworkout.training.model.ScheduleState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.util.*

class HistoryViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val firestore = Firebase.firestore
    var num: Double = 0.0
    var min: Double = 0.0
    var exercises: String = ""
    private val _state: MutableLiveData<HistoryState> = MutableLiveData(HistoryState.LOADING)
    val state: LiveData<HistoryState> get() = _state


    fun indicator(date: String){
        firestore.collection("Calendar")
            .whereEqualTo("user_id", auth.currentUser!!.uid)
            .whereEqualTo("date", date)
            .get().addOnSuccessListener { documents ->
                for(it in documents){
                    val calo = it.get("total_calories") as Double
                    num += calo
                    num = Math.round(num * 100) / 100.0

                    val milliseconds = it.get("total_time") as Double
                    min += milliseconds/60000
                    min = Math.round(min * 100) / 100.0

                }
                _state.value = HistoryState.LOADED
            }.addOnFailureListener {
                Log.d(ContentValues.TAG, "load failed", it)
            }
    }
}