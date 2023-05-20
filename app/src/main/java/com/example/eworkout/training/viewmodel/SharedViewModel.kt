package com.example.eworkout.training.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eworkout.training.model.Exercise
import com.example.eworkout.training.model.UpdateState
import com.example.eworkout.training.model.WorkoutDetail1State
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SharedViewModel : ViewModel() {
    private val firestore = Firebase.firestore

    private val auth = Firebase.auth

    private var kcalConsumed = 0.0;

    private val exercises = mutableListOf<Exercise>()

    lateinit var setTakenID: String

    private var currentExerciseIndex = 0

    private var currentWeight = 0.0

    private val _updateState : MutableLiveData<UpdateState> = MutableLiveData(UpdateState.RUNNING)
    val updateState : LiveData<UpdateState> get() = _updateState

    fun calculateKcal(sec: Long)
    {
        firestore.collection("Users").document(auth.currentUser!!.uid)
            .get().addOnSuccessListener {
                currentWeight = it.getDouble("current_weight")!!

                kcalConsumed += (((getCurrentExercise().MET * currentWeight * 3.5) / 200) / 60) * sec
            }
    }

    fun calculateAndUpdate(sec: Long)
    {
        firestore.collection("Users").document(auth.currentUser!!.uid)
            .get().addOnSuccessListener {
                currentWeight = it.getDouble("current_weight")!!

                kcalConsumed += (((getCurrentExercise().MET * currentWeight * 3.5) / 200) / 60) * sec

                updateSetTaken()
            }
    }
    fun addNewSetTaken(setId: String)
    {
        val data = hashMapOf(
            "user_id" to auth.currentUser!!.uid,
            "set_id" to setId,
            "start_time" to Timestamp.now(),
            "total_calories" to 0
        )
        firestore.collection("Set_Taken").add(data).addOnSuccessListener {
            setTakenID = it.id
        }
    }
    fun updateSetTaken()
    {
        val data = hashMapOf<String, Any>(
            "end_time" to Timestamp.now(),
            "total_calories" to Math.round(kcalConsumed * 100) / 100.0
        )
        firestore.collection("Set_Taken").document(setTakenID)
            .update(data)
            .addOnSuccessListener {
                _updateState.postValue(UpdateState.DONE)
            }
    }

    fun resetUpdateState()
    {
        _updateState.value = UpdateState.RUNNING
    }

    fun resetIndexAndCalories()
    {
        currentExerciseIndex = 0
        kcalConsumed = 0.0
    }


    fun getCustomSetFields(id: String)
    {
        exercises.clear()
        viewModelScope.launch(Dispatchers.IO) {
            val customSet = firestore.collection("Custom_Set")
                .document(id)
                .get().await()
            exercises.clear()

            findAllCustomSetInformation(id)
        }
    }

    private fun findAllCustomSetInformation(id: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val setInformation = firestore.collection("Custom_Set_Information")
                .whereEqualTo("set_id", id)
                .get().await()

            val exercisesIds = mutableListOf<String>()

            for (doc in setInformation.documents){
                exercisesIds.add(doc.getString("exercise_id").toString())
                Log.d("Workout Detail 1", doc.getString("exercise_id").toString())
            }
            findCustomExercisesByIds(exercisesIds)
        }
    }

    private fun findCustomExercisesByIds(ids: List<String>) {
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
                        doc.get("animation_url").toString(),
                        doc.getDouble("MET")!!)
                    exercises.add(exercise)
                }
                Log.d("Workout Detail 1","LOADED")
            }
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
                        doc.get("animation_url").toString(),
                        doc.getDouble("MET")!!)
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
        //Log.d("current", currentExerciseIndex.toString())
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