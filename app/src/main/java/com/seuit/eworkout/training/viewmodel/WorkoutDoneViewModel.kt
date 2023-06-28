package com.seuit.eworkout.training.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seuit.eworkout.training.model.WorkoutDoneModel
import com.seuit.eworkout.training.model.WorkoutDoneState
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class WorkoutDoneViewModel : ViewModel() {
    private val firestore = Firebase.firestore

    private val _state : MutableLiveData<WorkoutDoneState> = MutableLiveData(WorkoutDoneState.LOADING)
    val state : LiveData<WorkoutDoneState> get() = _state

    val modelLiveData: MutableLiveData<WorkoutDoneModel> = MutableLiveData()
    private var model: WorkoutDoneModel = WorkoutDoneModel("", 0.0, "")

    var currentSetId: String? = null

    var min: Double = 0.0

    fun getModelData(setTakenID: String, isSystemSet: Boolean)
    {
        firestore.collection("Set_Taken")
            .document(setTakenID)
            .get().addOnSuccessListener {
                Log.d("WorkoutDoneVM", setTakenID.toString())

                val calo = it.getDouble("total_calories")!!
                val milliseconds = (it.getDate("end_time")?.time!! - (it.getDate("start_time")?.time!!))
                min = milliseconds.toDouble()

                val simpledateformat = SimpleDateFormat("mm:ss")
                simpledateformat.timeZone = TimeZone.getTimeZone("UTC")
                val minutesAndSeconds = simpledateformat.format(Date(milliseconds))

                model.duration = minutesAndSeconds
                model.kcal = calo

                currentSetId = it.getString("set_id").toString()

                if(isSystemSet)
                    getExercisesQuantity(it.getString("set_id").toString())
                else
                    getCustomExercisesQuantity(it.getString("set_id").toString())
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

    private fun getCustomExercisesQuantity(set_id: String) {
        firestore.collection("Custom_Set").document(set_id)
            .get().addOnSuccessListener {
                model.exerciseQuantity = it.get("number_of_exercises").toString()
                modelLiveData.value = model
                _state.value = WorkoutDoneState.LOADED
            }
    }


    val auth = Firebase.auth
    var calendar_id: String? = null
    val calendar = Calendar.getInstance().time
    val dateFormat = DateFormat.getDateInstance().format(calendar)
    fun addToCalendar(setTakenId: String)
    {
        val data = hashMapOf(
            "user_id" to auth.currentUser!!.uid,
            "set_taken_id" to setTakenId,
            "date" to dateFormat,
            "total_calories" to model.kcal,
            "number_of_exercise" to model.exerciseQuantity.toDouble(),
            "total_time" to min
        )
        firestore.collection("Calendar").add(data).addOnSuccessListener {
            calendar_id = it.id
        }
    }
}