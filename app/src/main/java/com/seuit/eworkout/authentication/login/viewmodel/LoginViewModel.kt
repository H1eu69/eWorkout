package com.seuit.eworkout.authentication.login.viewmodel

import android.content.ContentValues
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seuit.eworkout.authentication.login.model.LoginState
import com.seuit.eworkout.authentication.signup.model.SignupState
import com.google.firebase.Timestamp
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
            val authResult = auth.signInWithCredential(credential).await()
                if(authResult.user != null){
                    if(authResult.additionalUserInfo!!.isNewUser){ //Create height and weight for new user
                        createInFireStore(false)
                    }
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
        val data1 = mutableMapOf(
            "email" to auth.currentUser?.email,
            "is_guest" to isGuest,
            "first_name" to "",
            "last_name" to "",
            "age" to 18,
            "current_weight" to 60.0,
            "current_height" to 165.0,
        )
        auth.currentUser?.let {
            firestore.collection("Users").document(it.uid).set(data1)

            val _BMI = (60.0/((165.0/100)*(165.0/100)))
            val data2 = hashMapOf(
                "user_id" to auth.currentUser!!.uid,
                "height" to 165.0,
                "weight" to 60.0,
                "time" to Timestamp.now(),
                "bmi" to _BMI
            )
            firestore.collection("Height_and_Weight").add(data2)
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