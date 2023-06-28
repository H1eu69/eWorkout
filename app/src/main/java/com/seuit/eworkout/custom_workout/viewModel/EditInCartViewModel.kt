package com.seuit.eworkout.custom_workout.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seuit.eworkout.custom_workout.model.AddToCartState
import com.seuit.eworkout.custom_workout.model.ExerciseInCart
import com.seuit.eworkout.custom_workout.model.ExerciseToAdd
import com.seuit.eworkout.custom_workout.model.ExerciseToAddDetail

class EditInCartViewModel : ViewModel() {
    private val _state : MutableLiveData<AddToCartState> = MutableLiveData(AddToCartState.REPS_CHOOSE)
    val state: LiveData<AddToCartState> get() = _state

    val exerciseInCart: MutableLiveData<ExerciseInCart> = MutableLiveData()

    fun changeState(stateToChange: AddToCartState)
    {
        _state.value = stateToChange
    }

    fun initExerciseInCart(bundle: Bundle) {
        when(bundle.getString("repType").toString())
        {
            "Reps" -> _state.value = AddToCartState.REPS_CHOOSE
            "Time" -> _state.value = AddToCartState.TIME_CHOOSE
        }
        val exercise = ExerciseInCart(
            bundle.getString("exercise_id").toString(),
            bundle.getString("name").toString(),
            bundle.getString("image").toString(),
            bundle.getInt("rep"),
            bundle.getString("repType").toString())
        exerciseInCart.value = exercise
    }

    fun changeRepType()
    {
        when(_state.value)
        {
            AddToCartState.REPS_CHOOSE -> exerciseInCart.value!!.repType = "Reps"
            else -> exerciseInCart.value!!.repType = "Time"
        }
    }
    fun increaseRepByOne()
    {
        exerciseInCart.value!!.reps += 1
        when(_state.value) //refresh UI
        {
            AddToCartState.REPS_CHOOSE -> _state.value = AddToCartState.REPS_CHOOSE
            else -> _state.value = AddToCartState.TIME_CHOOSE
        }
    }

    fun decreaseRepByOne()
    {
        if(exerciseInCart.value!!.reps > 0)
            exerciseInCart.value!!.reps -= 1
        when(_state.value) //To refresh UI
        {
            AddToCartState.REPS_CHOOSE -> _state.value = AddToCartState.REPS_CHOOSE
            else -> _state.value = AddToCartState.TIME_CHOOSE
        }
    }
}