package com.example.eworkout.authentication.login.viewmodel

import android.content.ContentValues
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.authentication.login.model.LoginState
import com.example.eworkout.authentication.signup.model.SignupState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class LoginViewModel: ViewModel() {
    val email: MutableLiveData<String> = MutableLiveData()

    val password: MutableLiveData<String> = MutableLiveData()

    private val _loginState: MutableLiveData<LoginState> = MutableLiveData()
    val loginState: LiveData<LoginState> get() = _loginState

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore

    suspend fun signInWithEmailAndPassword() {
        try {
            if(auth.signInWithEmailAndPassword(email.value.toString(), password.value.toString())
                    .await().user != null)
                _loginState.postValue(LoginState.SUCCESS)
        } catch (ex: Exception) {
            _loginState.postValue(LoginState.FAILED)
        }
    }

    suspend fun signInWithCredential(credential: AuthCredential)
    {
        try{
            if(auth.signInWithCredential(credential).await().user != null){
                createInFireStore(false)
                _loginState.postValue(LoginState.SUCCESS)
            }
        }
        catch (ex: Exception) {
            Log.d(ContentValues.TAG, ex.toString())
        }
    }
    suspend fun signInAnonymously()
    {
        try{
            if(auth.signInAnonymously().await().user != null) {
                createInFireStore(true)
                _loginState.postValue(LoginState.SIGN_IN_ANONYMOUS_SUCCESS)
            }
        }
        catch (ex: Exception) {
            Log.d(ContentValues.TAG, ex.toString())
        }
    }

    private fun createInFireStore(isGuest : Boolean)
    {
        val data = mutableMapOf(
            "email" to auth.currentUser?.email,
            "is_guest" to isGuest,
            "first_name" to "",
            "last_name" to "",
            "age" to 18,
            "current_weight" to 60.0,
            "current_height" to 165.0,
        )
        auth.currentUser?.let {
            firestore.collection("Users").document(it.uid).set(data)
        }
    }
    fun validateText(): Boolean
    {
        if(
            validateEmail() &&
            validatePassword()
        ) {
            return true
        }
        return false
    }

    fun validateEmail(): Boolean {
        if(email.value.isNullOrEmpty())
        {
            _loginState.value = LoginState.ERROR_EMAIL_EMPTY
            return false
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches())
        {
            _loginState.value = LoginState.ERROR_EMAIL_FORMAT
            return false
        }
        _loginState.value = LoginState.NO_ERROR_EMAIL
        return true
    }

    fun validatePassword(): Boolean {
        if(password.value.isNullOrEmpty())
        {
            _loginState.value = LoginState.ERROR_PASSWORD_EMPTY
            return false
        }
        else if(password.value.toString().length < 8)
        {
            _loginState.value = LoginState.ERROR_PASSWORD_LENGTH
            return false
        }
        _loginState.value = LoginState.NO_ERROR_PASSWORD
        return true
    }
}