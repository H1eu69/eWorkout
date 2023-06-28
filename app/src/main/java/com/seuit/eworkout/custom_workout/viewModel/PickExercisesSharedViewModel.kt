package com.seuit.eworkout.custom_workout.viewModel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seuit.eworkout.custom_workout.model.AddToCartState
import com.seuit.eworkout.custom_workout.model.CartState
import com.seuit.eworkout.custom_workout.model.ExerciseInCart
import com.seuit.eworkout.custom_workout.model.ExerciseToAdd
import com.seuit.eworkout.custom_workout.model.ExerciseToAddDetail
import com.seuit.eworkout.custom_workout.model.PickExercise
import com.seuit.eworkout.custom_workout.model.PickExercisesState
import com.seuit.eworkout.custom_workout.util.ConvertTypeUtil
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PickExercisesSharedViewModel : ViewModel() {
    val firestore = Firebase.firestore

    private val _state : MutableLiveData<PickExercisesState> = MutableLiveData(PickExercisesState.LOADING)
    val state: LiveData<PickExercisesState> get() = _state

    private val _list : MutableLiveData<List<PickExercise>> = MutableLiveData()
    val list : LiveData<List<PickExercise>> get() = _list

    val exercises = mutableListOf<PickExercise>()

    val filterExercises = mutableListOf<PickExercise>()

    private var exerciseInCart = mutableListOf<ExerciseToAdd>()

    private val _exercisesInCartLiveData : MutableLiveData<List<ExerciseToAdd>> = MutableLiveData(exerciseInCart)
    val exercisesInCartLiveData : LiveData<List<ExerciseToAdd>> get() = _exercisesInCartLiveData

    val storageRef = Firebase.storage.reference

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO


    fun getExercise(){
         viewModelScope.launch(defaultDispatcher) {
             exercises.clear()
             Log.d("test coroutine", "1")
             val docs = firestore.collection("Exercises")
                 .get().await()

             for (doc in docs.documents) {
                 exercises.add(
                     PickExercise(
                         doc.id,
                         "",
                         doc.get("name").toString(),
                         doc.get("level").toString(),
                         doc.get("muscle").toString(),
                         doc.get("description").toString(),
                         ""
                     )
                 )
                 Log.d("test coroutine", "2")
             }

             Log.d("test coroutine", "3")

             for(exercise in exercises)
             {
                 var path = "images/" + exercise.name + ".jpg"
                 exercise.image = storageRef.child(path)
                     .downloadUrl.await().toString()
                 path = "videos/" + exercise.name + ".mp4"
                 exercise.video = storageRef.child(path)
                     .downloadUrl.await().toString()
             }

             _list.postValue(exercises)
             _state.postValue(PickExercisesState.LOADED)
         }
    }

    fun filterByName(nameToFilter: String)
    {
        filterExercises.clear()
        if(nameToFilter.isEmpty())
        {
            _list.value = exercises
            _state.value = PickExercisesState.LOADED
        }
        else
        {
            if(!isExist(nameToFilter))
            {
                filterExercises.addAll(exercises.filter {
                    it.muscle.lowercase().contains(nameToFilter) || it.level.lowercase().contains(nameToFilter) || it.name.lowercase().contains(nameToFilter)
                })
            }
            _list.value = filterExercises
            _state.value = PickExercisesState.LOADED
        }
    }

    fun filter(filterTypes: List<String>)
    {
        filterExercises.clear()

        if(filterTypes.isEmpty())
        {
            _list.value = exercises
            _state.value = PickExercisesState.LOADED
        }
        else
        {
            for (type in filterTypes)
            {
                if(!isExist(type)){
                    filterExercises.addAll(exercises.filter {
                        it.muscle == type || it.level == type
                    })
                }
            }
            _list.value = filterExercises
            _state.value = PickExercisesState.LOADED
        }

    }

    fun isExist(s: String): Boolean
    {
        for (filter in filterExercises)
        {
            if(filter.muscle.lowercase().contains(s) || filter.level.lowercase().contains(s) || filter.name.lowercase().contains(s))
                return true
        }
        return false
    }

    fun changeStateToLoading()
    {
        _state.value = PickExercisesState.LOADING
    }

    fun addExerciseToCart(exerciseToAdd: ExerciseToAdd) {
        exerciseInCart.add(exerciseToAdd)
        _exercisesInCartLiveData.value = exerciseInCart
        Log.d("TEst add exercise in cart", _exercisesInCartLiveData.value!!.size.toString())
    }

    fun addExerciseToCart(exerciseToAdd: ExerciseToAddDetail) {
        val exercise = ExerciseToAdd(exerciseToAdd)
        exerciseInCart.add(exercise)
        _exercisesInCartLiveData.value = exerciseInCart
        Log.d("TEst add exercise in cart", _exercisesInCartLiveData.value!!.size.toString())
    }

    fun updateExercisesInCart(list: List<ExerciseInCart>)
    {
        exerciseInCart = ConvertTypeUtil.convertInCartToToAdd(list)
        _exercisesInCartLiveData.value = exerciseInCart
    }

    fun clearExerciseInCart()
    {
        exerciseInCart.clear()
    }

}