package com.seuit.eworkout.custom_workout.viewModel

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seuit.eworkout.custom_workout.model.ChooseImageState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.random.Random

class ChooseImageViewModel: ViewModel() {
    private var _uriLiveData: MutableLiveData<Uri?> = MutableLiveData()
    val uriLiveData: LiveData<Uri?> get() = _uriLiveData

    private var _state: MutableLiveData<ChooseImageState> = MutableLiveData()
    val state: LiveData<ChooseImageState> get() = _state

    val auth = Firebase.auth
    val storage = Firebase.storage

    fun updateImage(bundle: Bundle?)
    {
        var metadata = storageMetadata {
            contentType = "image/png"
        }

        val path = "custom_set/" + bundle?.getString("set_name") + "_" + auth.currentUser?.uid + "_" + bundle?.getLong("createdDate")
        storage.reference.child(path).putFile(uriLiveData.value!!, metadata)
            .addOnCompleteListener {
                _state.value = ChooseImageState.CREATED_NEW_IMAGE
            }
    }

    fun setUri(uri: Uri?)
    {
        _uriLiveData.value = uri
    }

    fun updateImage(bundle: Bundle?, bitmap: Bitmap?) {
        val path = "custom_set/" + bundle?.getString("set_name") + "_" + auth.currentUser?.uid + "_" + bundle?.getLong("createdDate")

        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        storage.reference.child(path).putBytes(data)
            .addOnCompleteListener {
                _state.value = ChooseImageState.CREATED_NEW_IMAGE
            }
    }

}