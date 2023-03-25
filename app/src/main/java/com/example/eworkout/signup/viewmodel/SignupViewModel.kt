package com.example.eworkout.signup.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.signup.model.SignupState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class SignupViewModel : ViewModel() {
    val firstName: MutableLiveData<String> = MutableLiveData()

    val lastName: MutableLiveData<String> = MutableLiveData()

    val email: MutableLiveData<String> = MutableLiveData()

    val password: MutableLiveData<String> = MutableLiveData()

    val confirmPassword: MutableLiveData<String> = MutableLiveData()

    private val _signupState: MutableLiveData<SignupState> = MutableLiveData()
    val signupState: LiveData<SignupState> get() = _signupState

    private val auth: FirebaseAuth = Firebase.auth

    suspend fun createUserWithEmailAndPassword() {
        try {
            if(auth.createUserWithEmailAndPassword(email.value.toString(), password.value.toString())
                .await().user != null)
                _signupState.postValue(SignupState.SUCCESS)
        } catch (ex: Exception) {
            Log.d(TAG, ex.toString())
            _signupState.postValue(SignupState.ERROR_USED_EMAIL)
        }
    }

    suspend fun signInWithCredential(credential: AuthCredential)
    {
        try{
            if(auth.signInWithCredential(credential).await().user != null)
                _signupState.postValue(SignupState.SUCCESS)
        }
        catch (ex: Exception) {
            Log.d(TAG, ex.toString())
        }
    }

    fun validateText(): Boolean
    {
        if(validateFirstName() &&
            validateLastName() &&
            validateEmail() &&
            validatePassword() &&
            validateConfirmPassword()
        ) {
            return true
        }
        return false
    }

    fun validateFirstName(): Boolean {
        if(firstName.value.isNullOrEmpty())
        {
            _signupState.value = SignupState.ERROR_FIRST_NAME_EMPTY
            return false
        }
        else if(firstName.value.toString().length < 3)
        {
            _signupState.value = SignupState.ERROR_FIRST_NAME_LENGTH
            return false
        }
        _signupState.value = SignupState.NO_ERROR_FIRST_NAME
        return true
    }

    fun validateLastName(): Boolean {
        if(lastName.value.isNullOrEmpty())
        {
            _signupState.value = SignupState.ERROR_LAST_NAME_EMPTY
            return false
        }
        else if(lastName.value.toString().length < 3)
        {
            _signupState.value = SignupState.ERROR_LAST_NAME_LENGTH
            return false
        }
        _signupState.value = SignupState.NO_ERROR_LAST_NAME
        return true
    }

    fun validateEmail(): Boolean {
        if(email.value.isNullOrEmpty())
        {
            _signupState.value = SignupState.ERROR_EMAIL_EMPTY
            return false
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches())
        {
            _signupState.value = SignupState.ERROR_EMAIL_FORMAT
            return false
        }
        _signupState.value = SignupState.NO_ERROR_EMAIL
        return true
    }
    fun validatePassword(): Boolean {
        if(password.value.isNullOrEmpty())
        {
            _signupState.value = SignupState.ERROR_PASSWORD_EMPTY
            return false
        }
        else if(password.value.toString().length < 8)
        {
            _signupState.value = SignupState.ERROR_PASSWORD_LENGTH
            return false
        }
        _signupState.value = SignupState.NO_ERROR_PASSWORD
        return true
    }

    fun validateConfirmPassword(): Boolean {
        if(confirmPassword.value.toString() != password.value.toString())
        {
            _signupState.value = SignupState.ERROR_CONFIRM_PASSWORD_MATCH
            return false
        }
        _signupState.value = SignupState.NO_ERROR_CONFIRM_PASSWORD
        return true
    }

}