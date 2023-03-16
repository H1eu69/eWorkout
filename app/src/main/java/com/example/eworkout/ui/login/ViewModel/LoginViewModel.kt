package com.example.eworkout.ui.login.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.ui.login.Model.LoginState
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

    suspend fun signInWithEmailAndPassword(): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email.value.toString(), password.value.toString())
                .await().user != null
        } catch (ex: Exception) {
            _loginState.postValue(LoginState.FAILED)
            false
        }
    }
}