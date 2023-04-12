package com.example.eworkout.training.viewmodel

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.eworkout.login.model.LoginState
import com.example.eworkout.training.model.Set
import com.example.eworkout.training.model.TrainingState
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class TrainingViewModel: ViewModel() {
    val firestore = Firebase.firestore

    val storageRef = Firebase.storage.reference

    val sets = mutableListOf<Set>()

    private val _state : MutableLiveData<TrainingState> = MutableLiveData(TrainingState.LOADING)
    val state : LiveData<TrainingState> get() = _state



    fun loadSets(){
        firestore.collection("Sets")
            .document("cV4RagVwciBDCqFfdfOi")
            .get()
            .addOnSuccessListener {data ->
                    val set = com.example.eworkout.training.model.Set(
                        data.id,
                        data.get("name").toString(),
                        data.get("total_time").toString(),
                        data.get("total_calories").toString(),
                        data.get("number_of_exercises").toString(),
                    ""
                    )
                    sets.add(set)
                    getUriImageByName(set)
                }
        firestore.collection("Sets")
            .document("z9BQfujVcuRKYs4d2DbW")
            .get()
            .addOnSuccessListener {data ->
                val set = com.example.eworkout.training.model.Set(
                    data.id,
                    data.get("name").toString(),
                    data.get("total_time").toString(),
                    data.get("total_calories").toString(),
                    data.get("number_of_exercises").toString(),
                    ""
                )
                sets.add(set)
                getUriImageByName(set)
            }
        firestore.collection("Sets")
            .document("bPGOVbrUNAnWf4y7dun1")
            .get()
            .addOnSuccessListener {data ->
                val set = com.example.eworkout.training.model.Set(
                    data.id,
                    data.get("name").toString(),
                    data.get("total_time").toString(),
                    data.get("total_calories").toString(),
                    data.get("number_of_exercises").toString(),
                    ""
                )
                sets.add(set)
                getUriImageByName(set)
            }
        firestore.collection("Sets")
            .document("CkU0yD2WlQweVEqqjupN")
            .get()
            .addOnSuccessListener {data ->
                val set = com.example.eworkout.training.model.Set(
                    data.id,
                    data.get("name").toString(),
                    data.get("total_time").toString(),
                    data.get("total_calories").toString(),
                    data.get("number_of_exercises").toString(),
                    ""
                )
                sets.add(set)
                getUriImageByName(set)
            }

        _state.value = TrainingState.LOADED
    }

    private fun getUriImageByName(set: com.example.eworkout.training.model.Set)
    {
        val path = "images/" + set.setName + ".svg"
        storageRef.child(path)
            .downloadUrl.addOnSuccessListener {
                set.setImage = it.toString()
                _state.value = TrainingState.IMAGE_LOADED
            }
            .addOnFailureListener {
                Log.d("adsa", it.message.toString())
            }
    }

}