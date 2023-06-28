package com.example.eworkout.training.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eworkout.custom_workout.model.CustomSet
import com.example.eworkout.training.model.HistoryState
import com.example.eworkout.training.model.Set
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HistoryViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val firestore = Firebase.firestore
    val storageRef = Firebase.storage.reference

    var num: Double = 0.0
    var min: Double = 0.0
    var exercises: Double = 0.0

    val sets = mutableListOf<Set>()
    val list_settakenid = mutableListOf<String>()
    val list_setid = mutableListOf<String>()

    private val _state: MutableLiveData<HistoryState> = MutableLiveData(HistoryState.LOADING)
    val state: LiveData<HistoryState> get() = _state


    fun loadSets(list: MutableList<String>){
        viewModelScope.launch(Dispatchers.IO)
        {
            var documents = firestore.collection("Sets")
                .whereIn(FieldPath.documentId(), list)
                .get().await()
            for(doc in documents){
                val set = Set(
                    doc.id,
                    doc.get("name").toString(),
                    doc.get("total_time") as Long,
                    doc.get("total_calories") as Long,
                    doc.get("number_of_exercises") as Long,
                    ""
                )
                getUriImageByName(set)
            }
            documents = firestore.collection("Custom_Set")
                .whereIn(FieldPath.documentId(), list)
                .get().await()

            for(doc in documents){
                val customSet = CustomSet(
                    doc.id,
                    doc.get("set_name").toString(),
                    doc.getLong("number_of_exercises")!!.toInt(),
                    "",
                    doc.getTimestamp("created_date")?.seconds!!
                )
                getUriImageByName(customSet)
            }

            _state.postValue(HistoryState.SET_LOADED)
        }
    }

    private fun getSetId(list: MutableList<String>){
        firestore.collection("Set_Taken")
            .whereIn(FieldPath.documentId(), list)
            .get()
            .addOnSuccessListener {
                for(data in it.documents){
                    list_setid.add(data.get("set_id").toString())
                    Log.d("_setid", data.get("set_id").toString())
                }
                loadSets(list_setid)
            }

    }

    fun getSetTakenId(date: String, date1: String) {
        firestore.collection("Calendar")
            .whereEqualTo("user_id", auth.currentUser!!.uid)
            .whereIn("date", listOf(date, date1))
            .get()
            .addOnSuccessListener {
                for(it in it.documents){
                    list_settakenid.add(it.get("set_taken_id").toString())
                    Log.d("History", it.getString("set_taken_id").toString())
                }
                if(list_settakenid.isNotEmpty())
                {
                    getSetId(list_settakenid)
                }
                Log.d("list_settakenid",list_settakenid.toString())
            }
    }

    private fun getUriImageByName(set: Set) {
        val path = "images/" + set.setName + ".png"
        storageRef.child(path)
            .downloadUrl.addOnSuccessListener {
                set.setImage = it.toString()
                _state.value = HistoryState.IMAGE_LOADED
                sets.add(set)
            }
            .addOnFailureListener {
                Log.d("IMAGE", it.message.toString())
            }
    }
    private fun getUriImageByName(set: CustomSet) {
        val path = "custom_set/" + set.name + "_" + auth.currentUser?.uid + "_" + set.createdDate
        storageRef.child(path)
            .downloadUrl.addOnSuccessListener {
                set.image = it.toString()
                _state.value = HistoryState.IMAGE_LOADED
                sets.add(set.toSet())
            }

    }
    fun indicator(date: String, date1: String){
        firestore.collection("Calendar")
            .whereEqualTo("user_id", auth.currentUser!!.uid)
            .whereIn("date", listOf(date, date1))
            .get().addOnSuccessListener { documents ->
                for(it in documents){
                    val calo = it.get("total_calories") as Double
                    num += calo
                    num = Math.round(num * 100) / 100.0

                    val milliseconds = it.get("total_time") as Double
                    min += milliseconds/60000
                    min = Math.round(min * 100) / 100.0

                    val ex = it.get("number_of_exercise") as Double
                    exercises += ex
                }
                _state.value = HistoryState.LOADED
            }.addOnFailureListener {
                Log.d(ContentValues.TAG, "load failed", it)
            }
    }
}