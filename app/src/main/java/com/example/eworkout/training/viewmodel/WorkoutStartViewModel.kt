package com.example.eworkout.training.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.training.model.UpdateState
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WorkoutStartViewModel : ViewModel() {
    private val firestore = Firebase.firestore

    private val auth = Firebase.auth

    private var kcalConsumed = 0.0;

    lateinit var setTakenID: String

    private var currentWeight = 0.0

    private val _updateState : MutableLiveData<UpdateState> = MutableLiveData(UpdateState.RUNNING)
    val updateState : LiveData<UpdateState> get() = _updateState

    fun calculateKcal(sec: Long, MET: Double)
    {
        firestore.collection("Users").document(auth.currentUser!!.uid)
            .get().addOnSuccessListener {
                currentWeight = it.getDouble("current_weight")!!

                kcalConsumed += (((MET * currentWeight * 3.5) / 200) / 60) * sec
            }
    }

    fun calculateAndUpdate(sec: Long, MET: Double, ID: String)
    {
        firestore.collection("Users").document(auth.currentUser!!.uid)
            .get().addOnSuccessListener {
                currentWeight = it.getDouble("current_weight")!!

                kcalConsumed += (((MET * currentWeight * 3.5) / 200) / 60) * sec

                updateSetTaken(ID)
            }
    }

    private fun updateSetTaken(ID: String)
    {
        val data = hashMapOf<String, Any>(
            "end_time" to Timestamp.now(),
            "total_calories" to Math.round(kcalConsumed * 100) / 100.0
        )
        firestore.collection("Set_Taken").document(ID)
            .update(data)
            .addOnSuccessListener {
                _updateState.value = UpdateState.DONE
            }
    }
}