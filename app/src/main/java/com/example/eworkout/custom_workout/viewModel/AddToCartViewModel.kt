package com.example.eworkout.custom_workout.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.custom_workout.model.AddToCartState
import com.example.eworkout.custom_workout.model.ExerciseInCart

class AddToCartViewModel : ViewModel() {
    private val _state : MutableLiveData<AddToCartState> = MutableLiveData(AddToCartState.REPS_CHOOSE)
    val state: LiveData<AddToCartState> get() = _state

    val exerciseInCart: MutableLiveData<ExerciseInCart> = MutableLiveData()

    fun changeState(stateToChange: AddToCartState)
    {
        _state.value = stateToChange
    }

    fun initExerciseInCart(bundle: Bundle) {
        val exercise = ExerciseInCart(
            bundle.getString("name").toString(),
            bundle.getString("image").toString(),
            15,
            "Reps"
        )
        exerciseInCart.value = exercise
    }

    fun increaseRepByOne()
    {
        exerciseInCart.value!!.reps += 1
        when(_state.value)
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