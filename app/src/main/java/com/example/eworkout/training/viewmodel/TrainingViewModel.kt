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

    val setName: MutableLiveData<String> = MutableLiveData()

    val setExercises: MutableLiveData<String> = MutableLiveData()

    val setTime: MutableLiveData<String> = MutableLiveData()

    private val _state : MutableLiveData<TrainingState> = MutableLiveData(TrainingState.LOADING)
    val state : LiveData<TrainingState> get() = _state



    fun loadSets(){
        firestore.collection("Sets")
            .get()
            .addOnSuccessListener {
                if(!it.isEmpty)
                {
                    for(data in it.documents)
                    {
                        val set = Set(
                            data.id,
                            data.get("name").toString(),
                            data.get("total_time").toString(),
                            data.get("total_calories").toString(),
                            data.get("number_of_exercises").toString()
                        )
                        sets.add(set)
                    }
                }
            }
    }

    /*fun getSetsFieldsById(id: String) {
        firestore.collection("Sets")
            .document(id)
            .get()
            .addOnSuccessListener {

                setName.value = it.getString("name")

                setExercises.value = it.getString("number_of_exercises")

                setTime.value = it.getString("total_time")

                findAllSetInformationBySetId(id)
                //Log.d("Workout Detail 1", it.getString("name").toString())
            }
            .addOnFailureListener {
                Log.d("Workout Detail 1", it.message.toString())
            }
    }

    private fun findAllSetInformationBySetId(id: String) {
        firestore.collection("Set_Information")
            .whereEqualTo("set_id", id)
            .get()
            .addOnSuccessListener {
                val SetsIds = mutableListOf<String>()
                for (doc in it.documents){
                    SetsIds.add(doc.getString("set_id").toString())
                    //Log.d("Workout Detail 1", doc.getString("exercise_id").toString())
                }
                findSetsByIds(SetsIds)
            }
    }

    private fun findSetsByIds(ids: List<String>) {
        firestore.collection("Sets")
            .whereIn(FieldPath.documentId(), ids)
            .get()
            .addOnSuccessListener {
                for (doc in it.documents){
                    val set = Set (
                        doc.id,
                        doc.get("name").toString(),
                        doc.get("total_time").toString(),
                        doc.get("total_calories").toString(),
                        doc.get("number_of_exercises").toString() )
                    sets.add(set)
                    //getUriImageByName(exercise)
                }
                _state.value = TrainingState.LOADED
                //Log.d("Workout Detail 1","LOADED")
            }
    }*/
}