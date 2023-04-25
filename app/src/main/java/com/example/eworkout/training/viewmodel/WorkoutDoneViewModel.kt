package com.example.eworkout.training.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.training.model.WorkoutDoneModel
import com.example.eworkout.training.model.WorkoutDoneState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class WorkoutDoneViewModel : ViewModel() {
    private val firestore = Firebase.firestore

    private val _state : MutableLiveData<WorkoutDoneState> = MutableLiveData(WorkoutDoneState.LOADING)
    val state : LiveData<WorkoutDoneState> get() = _state

    val modelLiveData: MutableLiveData<WorkoutDoneModel> = MutableLiveData()
    private var model: WorkoutDoneModel = WorkoutDoneModel("", 0.0, "")

    var currentSetId: String? = null

    fun getModelData(setTakenID: String)
    {
        firestore.collection("Set_Taken")
            .document(setTakenID)
            .get().addOnSuccessListener {
                val calo = it.getDouble("total_calories")!!
                val milliseconds = (it.getDate("end_time")?.time!! - (it.getDate("start_time")?.time!!))

                val simpledateformat = SimpleDateFormat("mm:ss")
                simpledateformat.timeZone = TimeZone.getTimeZone("UTC")
                val minutesAndSeconds = simpledateformat.format(Date(milliseconds))

                model.duration = minutesAndSeconds
                model.kcal = calo

                currentSetId = it.getString("set_id").toString()

                getExercisesQuantity(it.getString("set_id").toString())
            }
            .addOnFailureListener {
                Log.d("workout done failed", it.localizedMessage)
            }
    }

    private fun getExercisesQuantity(set_id: String) {
        firestore.collection("Sets").document(set_id)
            .get().addOnSuccessListener {
                model.exerciseQuantity = it.get("number_of_exercises").toString()
                modelLiveData.value = model
                _state.value = WorkoutDoneState.LOADED
            }
    }
}