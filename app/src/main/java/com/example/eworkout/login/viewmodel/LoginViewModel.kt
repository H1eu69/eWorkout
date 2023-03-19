package com.example.eworkout.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.login.model.LoginState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class LoginViewModel: ViewModel() {
    val email: MutableLiveData<String> = MutableLiveData()

    val password: MutableLiveData<String> = MutableLiveData()

    private val _loginState: MutableLiveData<LoginState> = MutableLiveData()
    val loginState: LiveData<LoginState> get() = _loginState

    private val auth: FirebaseAuth = Firebase.auth

    suspend fun signInWithEmailAndPassword() {
        try {
            if(auth.signInWithEmailAndPassword(email.value.toString(), password.value.toString())
                    .await().user != null)
                _loginState.postValue(LoginState.SUCCESS)
        } catch (ex: Exception) {
            _loginState.postValue(LoginState.FAILED)
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