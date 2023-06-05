package com.example.eworkout.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileViewModel: ViewModel() {
    private val auth = Firebase.auth

    fun logout()
    {
        auth.signOut()
    }
}