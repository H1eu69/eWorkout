package com.example.eworkout.training.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
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
import java.text.*
import java.util.*

class TrainingViewModel: ViewModel() {
    val firestore = Firebase.firestore
    val storageRef = Firebase.storage.reference

    val sets = mutableListOf<Set>()

    private val _state: MutableLiveData<TrainingState> = MutableLiveData(TrainingState.LOADING)
    val state: LiveData<TrainingState> get() = _state

    private val auth: FirebaseAuth = Firebase.auth
    private val user:FirebaseUser? = auth.currentUser

    var userEmail: String = ""
    var num: Double = 0.0
    var min: Double = 0.0

    val calendar = Calendar.getInstance().time
    val dateFormat = DateFormat.getDateInstance().format(calendar)

    fun getCurrentUserEmail(){
        if (user != null)
        {
            userEmail = user.email!!.substringBefore('@',userEmail)
        }
    }

    fun String.substringBefore(delimiter: Char, missingDelimiterValue: String = this): String {
        val index = indexOf(delimiter)
        return if (index == -1) missingDelimiterValue else substring(0, index)
    }


    fun indicatorWatching(){
        firestore.collection("Calendar")
            .whereEqualTo("user_id", auth.currentUser!!.uid)
            .whereEqualTo("date", dateFormat)
            .get().addOnSuccessListener { documents ->
                for(it in documents){
                    val calo = it.get("total_calories") as Double
                    num += calo
                    num = Math.round(num * 100) / 100.0

                    val milliseconds = it.get("total_time") as Double
                    min += milliseconds/60000
                    min = Math.round(min * 100) / 100.0


                    _state.value = TrainingState.LOADED
                }
            }.addOnFailureListener {
                Log.d(TAG, "load failed", it)
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
                getUriImageByName(set)
                }
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