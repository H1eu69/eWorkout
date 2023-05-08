package com.example.eworkout.custom_workout.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.custom_workout.util.ConvertTypeUtil
import com.example.eworkout.custom_workout.model.CartState
import com.example.eworkout.custom_workout.model.ExerciseInCart
import com.example.eworkout.custom_workout.model.ExerciseToAdd

class CartViewModel: ViewModel() {
    private val _state : MutableLiveData<CartState> = MutableLiveData(CartState.CHECKING_CART)
    val state: LiveData<CartState> get() = _state

    private var exerciseInCart = mutableListOf<ExerciseInCart>()

    private var _exercisesInCartLiveData : MutableLiveData<List<ExerciseInCart>> = MutableLiveData(exerciseInCart)
    val exercisesInCartLiveData: LiveData<List<ExerciseInCart>> get() = _exercisesInCartLiveData

    fun getExerciseFromSharedVM(listLiveData: List<ExerciseToAdd>?)
    {
        exerciseInCart = ConvertTypeUtil.convert(listLiveData)
        _exercisesInCartLiveData.value = exerciseInCart

    }

    fun isEmptyCart()
    {
        if(_exercisesInCartLiveData.value!!.isEmpty())
        {
            _state.value = CartState.EMPTY_CART
        }
        else
        {
            _state.value = CartState.NOT_EMPTY_CART
        }
    }


}