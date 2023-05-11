package com.example.eworkout.custom_workout.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.custom_workout.model.ChooseNameState

class CustomCreateSetChooseNameViewModel : ViewModel() {
    val setName : MutableLiveData<String> = MutableLiveData()
    private val _state : MutableLiveData<ChooseNameState> = MutableLiveData()
    val state: LiveData<ChooseNameState> get() = _state
    fun validateText(): Boolean
    {
        if(setName.value.isNullOrEmpty())
        {
            _state.value = ChooseNameState.INVALID
            return false
        }
        _state.value = ChooseNameState.VALID
        return true
    }
}