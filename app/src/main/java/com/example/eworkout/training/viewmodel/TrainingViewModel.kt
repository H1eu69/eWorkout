package com.example.eworkout.training.viewmodel

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.training.model.Set
import com.example.eworkout.training.model.TrainingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class TrainingViewModel: ViewModel() {
    val firestore = Firebase.firestore

    val storageRef = Firebase.storage.reference

    val sets = mutableListOf<Set>()

    var numberOfCalories: Double = 0.0

    var workoutHours: Long = 0

    private val _state: MutableLiveData<TrainingState> = MutableLiveData(TrainingState.LOADING)
    val state: LiveData<TrainingState> get() = _state

    private val auth: FirebaseAuth = Firebase.auth
    private val user:FirebaseUser? = auth.currentUser
    var userEmail:String = ""

    fun getCurrentUserEmail(){
        if (user != null)
        {
            userEmail = user.email.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun indicatorWatching(id: String) {
        firestore.collection("Set_Taken")
            .document(id)
            .get().addOnSuccessListener { data ->
                numberOfCalories += data.get("total_calories") as Double
                //workoutHours += data.get("time") as Long
            }
    }

    fun loadSets() {
        /*firestore.collection("Sets")
            .document("cV4RagVwciBDCqFfdfOi")
            .get()
            .addOnSuccessListener { data ->
                val set = Set(
                    data.id,
                    data.get("name").toString(),
                    data.get("total_time") as Long,
                    data.get("total_calories") as Long,
                    data.get("number_of_exercises") as Long,
                    ""
                )
                sets.add(set)
                getUriImageByName(set)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        firestore.collection("Sets")
            .document("z9BQfujVcuRKYs4d2DbW")
            .get()
            .addOnSuccessListener { data ->
                val set = Set(
                    data.id,
                    data.get("name").toString(),
                    data.get("total_time") as Long,
                    data.get("total_calories") as Long,
                    data.get("number_of_exercises") as Long,
                    ""
                )
                sets.add(set)
                getUriImageByName(set)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        firestore.collection("Sets")
            .document("bPGOVbrUNAnWf4y7dun1")
            .get()
            .addOnSuccessListener { data ->
                val set = Set(
                    data.id,
                    data.get("name").toString(),
                    data.get("total_time") as Long,
                    data.get("total_calories")as Long,
                    data.get("number_of_exercises") as Long,
                    ""
                )
                sets.add(set)
                getUriImageByName(set)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        firestore.collection("Sets")
            .document("CkU0yD2WlQweVEqqjupN")
            .get()
            .addOnSuccessListener { data ->
                val set = Set(
                    data.id,
                    data.get("name").toString(),
                    data.get("total_time") as Long,
                    data.get("total_calories") as Long,
                    data.get("number_of_exercises") as Long,
                    ""
                )
                sets.add(set)
                getUriImageByName(set)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }*/
        firestore.collection("Sets")
            .get()
            .addOnSuccessListener { documents ->
                for(data in documents){
                val set = Set(
                    data.id,
                    data.get("name").toString(),
                    data.get("total_time") as Long,
                    data.get("total_calories") as Long,
                    data.get("number_of_exercises") as Long,
                    ""
                )
                sets.add(set)
                getUriImageByName(set)}
                _state.value = TrainingState.LOADED
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }

    private fun getUriImageByName(set: Set) {
        val path = "images/" + set.setName + ".png"
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