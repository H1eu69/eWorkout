package com.example.eworkout.training.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eworkout.training.model.Exercise
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.eworkout.training.model.WorkoutDetail1State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class Workout1ViewModel : ViewModel() {
    val firestore = Firebase.firestore

    val exercises = mutableListOf<Exercise>()

    val setsName: MutableLiveData<String> = MutableLiveData()

    val setsInformation: MutableLiveData<String> = MutableLiveData()

    val state = MutableLiveData(WorkoutDetail1State.LOADING)

    fun getDocumentFields(id: String) {
        firestore.collection("Sets")
            .document(id)
            .get()
            .addOnSuccessListener {
                val numOfExercise = it.getDouble("number_of_exercises")?.toInt().toString()

                val totalCalories = it.getDouble("total_calories").toString()

                setsName.value = it.getString("name").toString()

                setsInformation.value = "$numOfExercise Exercise | $totalCalories Calories Burn"

                findAllSetInformation(id)
                Log.d("Workout Detail 1", it.getString("name").toString())
            }
            .addOnFailureListener {
                Log.d("Workout Detail 1", it.message.toString())
            }
    }

    private fun findAllSetInformation(id: String) {
        firestore.collection("Set_Information")
            .whereEqualTo("set_id", id)
            .get()
            .addOnSuccessListener {
                for (doc in it.documents){
                    findAllExercises(doc.getString("exercise_id").toString())
                    Log.d("Workout Detail 1", doc.getString("exercise_id").toString())
                }
            }
    }

    private fun findAllExercises(id: String) {
        firestore.collection("Exercises")
            .document(id)
            .get()
            .addOnSuccessListener {
                exercises.add(
                    Exercise(
                        it.get("name").toString(),
                        "",
                        it.get("reps").toString(),
                        it.get("description").toString(),
                        it.get("calories").toString(),
                        it.get("instruction").toString()
                    )
                )
                Log.d("Workout Detail 1", it.get("reps").toString())
            }
            .addOnCompleteListener {
                state.value = WorkoutDetail1State.FETCHED_NEW_DATA
                Log.d("Workout Detail 1","LOADED")
            }
    }
}
