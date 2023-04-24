package com.example.eworkout.training.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.training.model.Exercise
import com.example.eworkout.training.model.WorkoutDetail1State
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class SharedViewModel : ViewModel() {
    private val firestore = Firebase.firestore

    private val auth = Firebase.auth

    private var kcalConsumed = 0;

    private val storageRef = Firebase.storage.reference

    private val exercises = mutableListOf<Exercise>()

    lateinit var setTakenID: String

    private var currentExerciseIndex = 0

    fun addNewSetTaken(setId: String)
    {
        val data = hashMapOf(
            "user_id" to "O0PNnHMqAufapue91s5Zhf4TDTg1",
            "set_id" to setId,
            "start_time" to Timestamp.now(),
            "total_calories" to "0"
        )
        /*val data = hashMapOf(
            "user_id" to "auth.currentuser.uid",
            "set_id" to setId,
            "start_time" to Timestamp.now(),
            "total_calories" to "0"
        )*/
        firestore.collection("Set_Taken").add(data).addOnSuccessListener {
            setTakenID = it.id
        }
    }

    fun updateSetTaken()
    {
        firestore.collection("Set_Taken").document(setTakenID)
            .update("end_time", Timestamp.now())
    }

    fun getSetsFieldsById(id: String) {
        exercises.clear()
        firestore.collection("Sets")
            .document(id)
            .get()
            .addOnSuccessListener {
                exercises.clear()

                findAllSetInformationBySetId(id)
                Log.d("Workout Detail 1", it.getString("name").toString())
            }
            .addOnFailureListener {
                Log.d("Workout Detail 1", it.message.toString())
            }
    }

    private fun findAllSetInformationBySetId(id: String) {
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
                        doc.get("instruction").toString(),
                        doc.get("animation_url").toString())
                    exercises.add(exercise)
                }
                Log.d("Workout Detail 1","LOADED")
            }
    }

    fun increaseCurrentExerciseIndex(): Boolean
    {
        if(!isLastExercise())
        {
            currentExerciseIndex += 1
            return true
        }
        return false
    }
    fun decreaseCurrentExerciseIndex()
    {
        if(!isFirstExercise())
            currentExerciseIndex -= 1
    }

    fun showExercisesUntilFinish(): String
    {
        return "Next " + (currentExerciseIndex + 1).toString() + "/" + exercises.size
    }

    fun getCurrentExercise(): Exercise {
        return exercises[currentExerciseIndex]
    }

    fun isLastExercise(): Boolean
    {
        return currentExerciseIndex == exercises.size - 1
    }

    fun isFirstExercise(): Boolean
    {
        return currentExerciseIndex == 0
    }

}