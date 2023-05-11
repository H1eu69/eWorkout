package com.example.eworkout.custom_workout.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eworkout.custom_workout.model.CartState
import com.example.eworkout.custom_workout.model.ExerciseInCart
import com.example.eworkout.custom_workout.model.ExerciseToAdd
import com.example.eworkout.custom_workout.util.ConvertTypeUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.sql.Timestamp


class CartViewModel: ViewModel() {
    private val _state : MutableLiveData<CartState> = MutableLiveData(CartState.CHECKING_CART)
    val state: LiveData<CartState> get() = _state

    private var exerciseInCart = mutableListOf<ExerciseInCart>()

    private var _exercisesInCartLiveData : MutableLiveData<List<ExerciseInCart>> = MutableLiveData(exerciseInCart)
    val exercisesInCartLiveData: LiveData<List<ExerciseInCart>> get() = _exercisesInCartLiveData

    var itemChangePosition = -1
    private var orderNumber = 0
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
    val firestore = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    fun getExerciseFromSharedVM(listLiveData: List<ExerciseToAdd>?)
    {
        exerciseInCart = ConvertTypeUtil.convertToAddToInCart(listLiveData)
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

    fun updateExerciseInCart(exercise: ExerciseInCart?) {
        if(exercise != null) {
            val updateExercise = exerciseInCart.find {
                it.name == exercise.name
            }?.apply {
                reps = exercise.reps
                repType = exercise.repType
            }
            itemChangePosition = exerciseInCart.indexOf(updateExercise)
            _state.value = CartState.ELEMENT_UPDATED
        }
    }

    fun deleteExerciseInCart(bundle: Bundle) {
        val exercise = ExerciseInCart(
            bundle.getString("exercise_id").toString(),
            bundle.getString("name").toString(),
            bundle.getString("image").toString(),
            bundle.getInt("reps"),
            bundle.getString("repType").toString()
        )
        val updateExercise = exerciseInCart.find {
            it.name == exercise.name
        }
        itemChangePosition = exerciseInCart.indexOf(updateExercise)
        exerciseInCart.remove(updateExercise)
        _state.value = CartState.ELEMENT_DELETED

        if(exerciseInCart.isEmpty())
            _state.value = CartState.EMPTY_CART

    }

    fun addToDb(arguments: Bundle?) {
        viewModelScope.launch(defaultDispatcher) {
            val setName = arguments?.getString("set_name")!!

            val createdDate = com.google.firebase.Timestamp.now()

            val customSetData: MutableMap<String, Any> = HashMap()
            customSetData["set_name"] = setName
            customSetData["number_of_exercises"] = exerciseInCart.size
            customSetData["user_id"] = auth.currentUser!!.uid
            customSetData["created_date"] = createdDate
            arguments.putLong("createdDate", createdDate.seconds)

            val set = firestore.collection("Custom_Set")
                .add(customSetData)
                .await()

            withContext(defaultDispatcher) {
                exerciseInCart.forEach {
                    val customSetInfoData: MutableMap<String, Any> = HashMap()
                    customSetInfoData["set_id"] = set.id
                    customSetInfoData["exercise_id"] = it.id
                    customSetInfoData["reps"] = it.reps
                    customSetInfoData["rep_Type"] = it.repType
                    customSetInfoData["orderNumber"] = orderNumber
                    firestore.collection("Custom_Set_Information")
                        .add(customSetInfoData)
                        .await()
                    orderNumber += 1
                }
            }
            _state.postValue(CartState.ADDED_NEW_SET)

        }
    }


}