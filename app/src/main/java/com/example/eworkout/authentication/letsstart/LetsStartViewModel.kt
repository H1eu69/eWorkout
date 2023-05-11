package com.example.eworkout.authentication.letsstart

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LetsStartViewModel: ViewModel() {
    private val auth = Firebase.auth

    fun hasSignInPreviously(): Boolean
    {
        return auth.currentUser != null
    }
}