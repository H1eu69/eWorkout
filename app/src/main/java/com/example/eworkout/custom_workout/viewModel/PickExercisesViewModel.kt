package com.example.eworkout.custom_workout.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eworkout.custom_workout.model.PickExercise
import com.example.eworkout.custom_workout.model.PickExercisesState
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PickExercisesViewModel : ViewModel() {
    val firestore = Firebase.firestore

    private val _state : MutableLiveData<PickExercisesState> = MutableLiveData(PickExercisesState.LOADING)
    val state: LiveData<PickExercisesState> get() = _state

    private val _list : MutableLiveData<List<PickExercise>> = MutableLiveData(ArrayList())
    val list : LiveData<List<PickExercise>> get() = _list

    val storageRef = Firebase.storage.reference

    val getExerciseTask = firestore.collection("Exercises").get()

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO

    fun getExercise(){
         viewModelScope.launch(defaultDispatcher) {
             Log.d("test coroutine", "1")
             val docs = firestore.collection("Exercises")
                 .get().await()

             val exercises = ArrayList<PickExercise>()
             for (doc in docs.documents) {
                 exercises.add(
                     PickExercise(
                         doc.id,
                         "",
                         doc.get("name").toString()
                     )
                 )
                 Log.d("test coroutine", "2")
             }

             Log.d("test coroutine", "3")

             for(exercise in exercises)
             {
                 val path = "images/" + exercise.name + ".jpg"
                 exercise.image = storageRef.child(path)
                     .downloadUrl.await().toString()
             }

             _list.postValue(exercises)
             _state.postValue(PickExercisesState.LOADED)
         }
    }

}