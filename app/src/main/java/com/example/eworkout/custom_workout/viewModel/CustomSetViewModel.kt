package com.example.eworkout.custom_workout.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eworkout.custom_workout.model.ChooseNameState
import com.example.eworkout.custom_workout.model.CustomSet
import com.example.eworkout.custom_workout.model.CustomSetState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CustomSetViewModel: ViewModel() {
    private val _state : MutableLiveData<CustomSetState> = MutableLiveData(CustomSetState.LOADING)
    val state: LiveData<CustomSetState> get() = _state

    var setList = mutableListOf<CustomSet>()
    private val _sets : MutableLiveData<List<CustomSet>> = MutableLiveData(setList)
    val sets: LiveData<List<CustomSet>> get() = _sets

    val firestore = Firebase.firestore
    val auth = Firebase.auth
    val storage = Firebase.storage
    val defaultDispatcher = Dispatchers.IO

    fun getCustomSet()
    {
        viewModelScope.launch(defaultDispatcher) {
            val docs = firestore.collection("Custom_Set")
                .whereEqualTo("user_id", auth.currentUser?.uid)
                .get()
                .await()

            for(doc in docs.documents)
            {
                val setName = doc.getString("set_name").toString()
                val numOfExercise = doc.getLong("number_of_exercises")!!.toInt()
                val createdDate = doc.getTimestamp("created_date")?.seconds!!
                setList.add(
                    CustomSet(
                        setName,
                        numOfExercise,
                        "",
                        createdDate
                    )
                )
            }

            for(set in setList)
            {
                val path = "custom_set/" + set.name + "_" + auth.currentUser?.uid + "_" + set.createdDate
                set.image = storage.reference.child(path)
                    .downloadUrl.await().toString()
            }
            _state.postValue(CustomSetState.LOADED)
        }
    }
}