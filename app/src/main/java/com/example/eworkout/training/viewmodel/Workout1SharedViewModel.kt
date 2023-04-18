package com.example.eworkout.training.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.training.model.Exercise
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.eworkout.training.model.WorkoutDetail1State
import com.google.firebase.firestore.FieldPath
import com.google.firebase.storage.ktx.storage

class Workout1SharedViewModel : ViewModel() {
    val firestore = Firebase.firestore

    val storageRef = Firebase.storage.reference

    val exercises = mutableListOf<Exercise>()

    val setsName: MutableLiveData<String> = MutableLiveData()

    val setsInformation: MutableLiveData<String> = MutableLiveData()

    private val _state : MutableLiveData<WorkoutDetail1State> = MutableLiveData(WorkoutDetail1State.LOADING)
    val state : LiveData<WorkoutDetail1State> get() = _state

    private var currentExerciseIndex = 0

    fun getSetsFieldsById(id: String) {
        firestore.collection("Sets")
            .document(id)
            .get()
            .addOnSuccessListener {
                val numOfExercise = it.getDouble("number_of_exercises")?.toInt().toString()

                val totalCalories = it.getDouble("total_calories").toString()

                setsName.value = it.getString("name").toString()

                setsInformation.value = "$numOfExercise Exercise | $totalCalories Calories Burn"

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
                    getUriImageByName(exercise)
                }
                _state.value = WorkoutDetail1State.LOADED
                Log.d("Workout Detail 1","LOADED")
            }
    }

    private fun getUriImageByName(exercise: Exercise)
    {
        val path = "images/" + exercise.name + ".jpg"
        storageRef.child(path)
            .downloadUrl.addOnSuccessListener {
                exercise.image = it.toString()
                _state.value = WorkoutDetail1State.IMAGE_LOADED
            }
    }

    fun increaseCurrentExerciseIndex()
    {
        if(currentExerciseIndex < exercises.size - 1)
            currentExerciseIndex += 1
    }
    fun decreaseCurrentExerciseIndex()
    {
        if(currentExerciseIndex > 0)
            currentExerciseIndex -= 1
    }

    fun showExercisesUntilFinish(): String
    {
        return "Next " + (currentExerciseIndex + 1).toString() + "/" + exercises.size
    }

    fun getCurrentExercise(): Exercise {
        return exercises[currentExerciseIndex]
    }
    fun getCurrentExerciseAndIncreaseIndex(): Exercise
    {
        val current = exercises[currentExerciseIndex]
        increaseCurrentExerciseIndex()
        return current
    }

    fun getNextExercise(): Exercise{
        return exercises[currentExerciseIndex + 1]
    }

}
