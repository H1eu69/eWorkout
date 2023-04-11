package com.example.eworkout.training.viewmodel

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.login.model.LoginState
import com.example.eworkout.training.model.Set
import com.example.eworkout.training.model.TrainingState
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class TrainingViewModel: ViewModel() {
    val firestore = Firebase.firestore

    val storageRef = Firebase.storage.reference

    val exercises = mutableListOf<Set>()

    val setsName: MutableLiveData<String> = MutableLiveData()

    val setsExercises: MutableLiveData<String> = MutableLiveData()

    val setsTime: MutableLiveData<String> = MutableLiveData()

    private val _state : MutableLiveData<TrainingState> = MutableLiveData(TrainingState.LOADING)
    val state : LiveData<TrainingState> get() = _state

    fun getSetsFieldsById(id: String) {
        firestore.collection("Sets")
            .document(id)
            .get()
            .addOnSuccessListener {

                setsName.value = it.getString("name").toString()

                setsExercises.value = it.getDouble("number_of_exercises")?.toInt().toString()

                setsTime.value = it.getDouble("total_time").toString()

                //findAllSetInformationBySetId(id)
                Log.d("Workout Detail 1", it.getString("name").toString())
            }
            .addOnFailureListener {
                Log.d("Workout Detail 1", it.message.toString())
            }
    }

    /*private fun findAllSetInformationBySetId(id: String) {
        firestore.collection("Set_Information")
            .whereEqualTo("set_id", id)
            .get()
            .addOnSuccessListener {
                val exercisesIds = mutableListOf<String>()
                for (doc in it.documents){
                    exercisesIds.add(doc.getString("exercise_id").toString())
                    Log.d("Workout Detail 1", doc.getString("exercise_id").toString())
                }
                findExercisesByIds(exercisesIds)
            }
    }

    private fun findExercisesByIds(ids: List<String>) {
        firestore.collection("Exercises")
            .whereIn(FieldPath.documentId(), ids)
            .get()
            .addOnSuccessListener {
                for (doc in it.documents){
                    val exercise = Exercise(
                        doc.id,
                        doc.get("name").toString(),
                        "",
                        doc.get("reps").toString(),
                        doc.get("description").toString(),
                        doc.get("calories").toString(),
                        doc.get("instruction").toString())
                    exercises.add(exercise)
                    getUriImageByName(exercise)
                }
                _state.value = WorkoutDetail1State.LOADED
                Log.d("Workout Detail 1","LOADED")
            }
    }*/

}