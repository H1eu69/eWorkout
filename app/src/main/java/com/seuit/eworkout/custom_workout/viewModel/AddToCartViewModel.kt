package com.seuit.eworkout.custom_workout.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seuit.eworkout.custom_workout.model.AddToCartState
import com.seuit.eworkout.custom_workout.model.ExerciseToAdd
import com.seuit.eworkout.custom_workout.model.ExerciseToAddDetail

class AddToCartViewModel : ViewModel() {
    private val _state : MutableLiveData<AddToCartState> = MutableLiveData(AddToCartState.REPS_CHOOSE)
    val state: LiveData<AddToCartState> get() = _state

    val exerciseToAdd: MutableLiveData<ExerciseToAdd> = MutableLiveData()

    val exerciseToAddDetail: MutableLiveData<ExerciseToAddDetail> = MutableLiveData()

    fun changeState(stateToChange: AddToCartState)
    {
        _state.value = stateToChange
    }

    fun initExerciseToAdd(bundle: Bundle) {
        val exercise = ExerciseToAdd(
            bundle.getString("exercise_id").toString(),
            bundle.getString("name").toString(),
            bundle.getString("image").toString(),
            15,
            "Reps"
        )
        exerciseToAdd.value = exercise
    }

    fun initExerciseToAddDetail(bundle: Bundle?) {
        val exercise = ExerciseToAddDetail(
            bundle?.getString("id").toString(),
            bundle?.getString("name").toString(),
            bundle?.getString("image").toString(),
            15,
            "Reps",
            bundle?.getString("video").toString(),
            bundle?.getString("description").toString(),
            bundle?.getString("level").toString()
        )
        exerciseToAddDetail.value = exercise
    }
    fun changeRepType()
    {
        when(_state.value)
        {
            AddToCartState.REPS_CHOOSE -> exerciseToAdd.value!!.repType = "Reps"
            else -> exerciseToAdd.value!!.repType = "Time"
        }
    }
    fun changeRepTypeDetail()
    {
        when(_state.value)
        {
            AddToCartState.REPS_CHOOSE -> exerciseToAddDetail.value!!.repType = "Reps"
            else -> exerciseToAddDetail.value!!.repType = "Time"
        }
    }
    fun increaseRepByOne()
    {
        exerciseToAdd.value!!.reps += 1
        when(_state.value)
        {
            AddToCartState.REPS_CHOOSE -> _state.value = AddToCartState.REPS_CHOOSE
            else -> _state.value = AddToCartState.TIME_CHOOSE
        }
    }

    fun decreaseRepByOne()
    {
        if(exerciseToAdd.value!!.reps > 0)
            exerciseToAdd.value!!.reps -= 1
        when(_state.value) //To refresh UI
        {
            AddToCartState.REPS_CHOOSE -> _state.value = AddToCartState.REPS_CHOOSE
            else -> _state.value = AddToCartState.TIME_CHOOSE
        }
    }
}