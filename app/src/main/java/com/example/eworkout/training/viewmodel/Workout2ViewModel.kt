package com.example.eworkout.training.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.training.model.Instruction
import com.example.eworkout.training.model.WorkoutDetail2State
import com.example.eworkout.training.util.StringUlti
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class Workout2ViewModel : ViewModel() {
    val exerciseName : MutableLiveData<String> = MutableLiveData()

    val exerciseInfo : MutableLiveData<String> = MutableLiveData()

    val exerciseDescription : MutableLiveData<String> = MutableLiveData()

    val instructions : MutableLiveData<String> = MutableLiveData()

    var instructionSteps = mutableListOf<Instruction>()

    private val _uri : MutableLiveData<Uri> = MutableLiveData()
    val uri : LiveData<Uri> get() = _uri

    private val _state : MutableLiveData<WorkoutDetail2State> = MutableLiveData(WorkoutDetail2State.LOADING)
    val state : LiveData<WorkoutDetail2State> get() = _state

    val firestore = Firebase.firestore

    val storage = Firebase.storage
    val storageRef = storage.reference

    fun get()
    {
        val path = "videos/" + exerciseName.value.toString() + ".mp4"
        storageRef.child(path)
            .downloadUrl.addOnSuccessListener {
                _uri.value = it
                _state.value = WorkoutDetail2State.VIDEO_LOADED
            }
    }

    fun getExerciseInfoById(id: String)
    {
        firestore.collection("Exercises")
            .document(id)
            .get()
            .addOnSuccessListener {
                exerciseName.value = it.getString("name").toString()
                exerciseInfo.value = it.getString("level").toString() + " | " + it.getString("calories").toString() + " calories / rep"
                exerciseDescription.value = it.getString("description").toString()
                instructions.value = it.get("instruction").toString()
                getInstructionStepByStep()
                _state.value = WorkoutDetail2State.LOADED
            }
    }

    private fun getInstructionStepByStep()
    {
        instructionSteps = StringUlti.subInstructionsIntoList(instructions.value!!)
    }

}