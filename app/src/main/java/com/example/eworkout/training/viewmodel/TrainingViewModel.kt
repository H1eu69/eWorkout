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
import com.example.eworkout.login.model.LoginState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class TrainingViewModel: ViewModel() {

    val db = Firebase.firestore

     fun getSetId(name: String): String{
        val setId = db.collection("Sets")
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, document.id)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
        return setId.toString()
    }
/*fun pickSet(){
        // creating the instance of the bundle
        val bundle = Bundle()

// storing the string value in the bundle
// which is mapped to key
        bundle.putString("exerciseId", "")

// creating a intent
        var intent = Intent(this@TrainingViewModel, SecondActivity::class.java)

// passing a bundle to the intent
        intent.putExtras(bundle)

// starting the activity by passing the intent to it.
        startActivity(intent)
    }*/

}


